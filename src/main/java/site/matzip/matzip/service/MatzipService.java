package site.matzip.matzip.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.rsData.RsData;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.dto.MatzipRankDTO;
import site.matzip.matzip.dto.MatzipUpdateDTO;
import site.matzip.matzip.repository.MatzipMemberRepository;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MemberRepository memberRepository;
    private final MatzipMemberRepository matzipMemberRepository;


    @CacheEvict(value = {"matzipListCache", "myMatzipListCache", "reviewListCache"}, allEntries = true)
    public Matzip create(MatzipCreationDTO creationDTO, Long authorId) {
        Optional<Matzip> optionalExistingMatzip = matzipRepository.findByKakaoId(creationDTO.getKakaoId());
        Member author = memberRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("member not found"));

        if (optionalExistingMatzip.isPresent()) {
            Matzip existingMatzip = optionalExistingMatzip.get();
            MatzipMember matzipRecommendation = createMatzipRecommendationEntity(creationDTO, existingMatzip, author);
            matzipMemberRepository.save(matzipRecommendation);
            return existingMatzip;
        } else {
            Matzip matzip = createMatzipEntity(creationDTO);
            Matzip savedMatzip = matzipRepository.save(matzip);
            MatzipMember matzipRecommendation = createMatzipRecommendationEntity(creationDTO, savedMatzip, author);
            matzipMemberRepository.save(matzipRecommendation);
            return savedMatzip;
        }
    }

    //맛집 엔티티 생성 메서드
    private Matzip createMatzipEntity(MatzipCreationDTO creationDTO) {
        return Matzip.builder()
                .matzipName(creationDTO.getMatzipName())
                .address(creationDTO.getAddress())
                .matzipType(creationDTO.getMatzipTypeEnum())
                .phoneNumber(creationDTO.getPhoneNumber())
                .matzipUrl(creationDTO.getMatzipUrl())
                .kakaoId(creationDTO.getKakaoId())
                .x(creationDTO.getX())
                .y(creationDTO.getY())
                .build();
    }

    //개인의 맛집 추천(후기) 엔티티 생성
    private MatzipMember createMatzipRecommendationEntity(MatzipCreationDTO creationDTO, Matzip savedMatzip, Member author) {
        MatzipMember createdMatzipMember = MatzipMember.builder()
                .rating(creationDTO.getRating())
                .description(creationDTO.getDescription())
                .build();
        createdMatzipMember.setAuthor(author);
        createdMatzipMember.setMatzip(savedMatzip);

        return createdMatzipMember;
    }

    public List<Matzip> findAll() {
        return matzipRepository.findAll();
    }

    public Matzip findById(Long id) {
        return matzipRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Matzip not found with id: " + id));
    }

    //사용자의 id로 맛집과 맛집_멤버 같이 호출
    public List<Matzip> findAllByAuthorId(Long authorId) {
        return matzipRepository.findAllByAuthorId(authorId);
    }

    //모든 맛집 정보와 리뷰 리스트 표시 위한 메서드, 사용자 정보 넣어주고 사용자 후기 없는 곳까지 표시함, dto로 변환까지 해서 반환
    @Cacheable(value = "matzipListCache")
    public List<MatzipListDTO> findAndConvertAll(Long authorId) {
        return convertToListDTO(findAll(), authorId);
    }

    //id로 등록한 맛집 정보 검색
    @Cacheable(value = "myMatzipListCache", key = "#authorId")
    public List<MatzipListDTO> findAndConvertById(Long authorId) {
        return convertToListDTO(findAllByAuthorId(authorId), authorId);
    }

    @CacheEvict(value = {"matzipListCache", "myMatzipListCache"}, allEntries = true)
    public RsData delete(Long matzipId, Long authorId) {
        MatzipMember matzipMember = matzipMemberRepository.findByMatzipIdAndAuthorId(matzipId, authorId).orElse(null);
        if (matzipMember == null) {
            return RsData.of("F-1", "이미 삭제된 맛집입니다");
        }
        Matzip matzip = matzipMember.getMatzip();
        matzipMemberRepository.delete(matzipMember);
        //누구의 맛집 지도에도 남아있지 않으면 맛집 자체를 삭제
        if (matzip.getMatzipMembers().isEmpty()) {
            matzipRepository.delete(matzip);
        }
        return RsData.of("S-1", "맛집이 삭제되었습니다.");
    }

    //후기와 맛집 정보를 하나로 묶어서 MatzipListDTO로 변환
    private List<MatzipListDTO> convertToListDTO(List<Matzip> matzipList, Long authorId) {
        return matzipList.stream().map(matzip -> {
            List<MatzipMember> matzipMemberList = matzip.getMatzipMembers();

            Optional<MatzipMember> authorRecommendation = matzipMemberList.stream()
                    .filter(recommendation -> recommendation.getAuthor().getId().equals(authorId))
                    .findFirst();

            double rating = 0;
            String description = "";
            //사용자 후기 존재하는 곳이면 유저 후기, 없으면 0, 빈칸
            if (authorRecommendation.isPresent()) {
                rating = authorRecommendation.get().getRating();
                description = authorRecommendation.get().getDescription();
            }
            // 리스트 DTO 생성
            return MatzipListDTO.builder()
                    .matzipName(matzip.getMatzipName())
                    .address(matzip.getAddress())
                    .phoneNumber(matzip.getPhoneNumber())
                    .matzipUrl(matzip.getMatzipUrl())
                    .matzipType(matzip.getMatzipType())
                    .x(matzip.getX())
                    .y(matzip.getY())
                    .rating(rating)
                    .averageRating(getAverageRating(matzip))
                    .description(description)
                    .matzipId(matzip.getId())
                    .build();
        }).collect(Collectors.toList());
    }

    @CacheEvict(value = {"matzipListCache", "myMatzipListCache"}, allEntries = true)
    public RsData modify(Long matzipId, Long authorId, MatzipUpdateDTO matzipUpdateDTO) {
        MatzipMember matzipMember = matzipMemberRepository.findByMatzipIdAndAuthorId(matzipId, authorId).orElse(null);
        if (matzipMember == null) {
            return RsData.of("F-1", "사용자의 후기를 찾을 수 없습니다.");
        }
        matzipMember.update(matzipUpdateDTO.getDescription(), matzipUpdateDTO.getRating());
        matzipMemberRepository.save(matzipMember);
        return RsData.of("S-1", "업데이트가 완료되었습니다.");
    }

    @Transactional
    public List<MatzipRankDTO> findAndConvertTopTenMatzip() {
        List<Matzip> matzips = getTop10ByOrderByMatzipMembersSize();
        return matzips.stream().map(this::convertToMatzipDTO).collect(Collectors.toList());
    }

    private List<Matzip> getTop10ByOrderByMatzipMembersSize() {
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        return matzipRepository.findTop10ByOrderByMatzipMembersSizeDesc(pageable);
    }

    private MatzipRankDTO convertToMatzipDTO(Matzip matzip) {

        return MatzipRankDTO.builder()
                .matzipName(matzip.getMatzipName())
                .averageRating(getAverageRating(matzip))
                .reviewCount(matzip.getReviews().size())
                .userCount(matzip.getMatzipMembers().size())
                .matzipUrl(matzip.getMatzipUrl())
                .build();
    }

    private double getAverageRating(Matzip matzip) {
        double sum = 0;

        for (MatzipMember matzipMember : matzip.getMatzipMembers()) {
            sum += matzipMember.getRating();
        }

        return sum / (double) matzip.getMatzipMembers().size();
    }
}