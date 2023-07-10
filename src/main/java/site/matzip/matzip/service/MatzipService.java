package site.matzip.matzip.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipMember;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.dto.MatzipReviewListDTO;
import site.matzip.matzip.repository.MatzipMemberRepository;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewListDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MemberRepository memberRepository;
    private final MatzipMemberRepository matzipMemberRepository;


    @CacheEvict(value = {"matzipListCache", "myMatzipListCache", "reviewListCache"}, allEntries = true)
    public Matzip create(MatzipCreationDTO creationDTO, Member author) {
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

    //리뷰 엔티티 만드는 메서드
    private Review createReviewEntity(Matzip matzip, ReviewCreationDTO reviewCreationDTO, Member author) {
        Review createdReview = Review.builder()
                .content(reviewCreationDTO.getContent())
                .rating(reviewCreationDTO.getRating())
                .build();
        createdReview.setMatzip(matzip);
        createdReview.setAuthor(author);

        return createdReview;
    }

    public List<Matzip> findAll() {
        return matzipRepository.findAll();
    }


    public Matzip findById(Long id) {
        return matzipRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Matzip not found with id: " + id));
    }

    //사용자의 맛집지도 속 맛집 호출
    public List<Matzip> findAllByAuthorId(Long authorId) {
        return matzipRepository.findAllByAuthorId(authorId);
    }

    //모든 맛집 정보와 리뷰 리스트 표시 위한 메서드, 사용자 정보 넣어주고 사용자 후기 없는 곳까지 표시함, dto로 변환까지 해서 반환
    @Cacheable(value = "matzipListCache")
    public List<MatzipListDTO> findAndConvertAll(Long authorId) {
        return convertToListDTO(findAll(), authorId);
    }

    //내가 등록한 맛집 정보 검색
    @Cacheable(value = "myMatzipListCache")
    public List<MatzipListDTO> findAndConvertMine(Long authorId) {
        return convertToListDTO(findAllByAuthorId(authorId), authorId);
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
                    .description(description)
                    .matzipId(matzip.getId())
                    .build();
        }).collect(Collectors.toList());
    }

    //컨트롤러에서 받은 리뷰DTO와 맛집DTO를 머지해서 하나로 만들어 준다.
    public List<MatzipReviewListDTO> mergeMatzipAndReviews(List<MatzipListDTO> matzipDtoList, List<ReviewListDTO> reviewDtoList) {
        List<MatzipReviewListDTO> matzipReviewList = new ArrayList<>();

        for (MatzipListDTO matzipListDTO : matzipDtoList) {
            List<ReviewListDTO> matchedReviews = reviewDtoList.stream()
                    .filter(review -> Objects.equals(review.getMatzipId(), matzipListDTO.getMatzipId()))
                    .collect(Collectors.toList());

            MatzipReviewListDTO matzipReviewListDTO = MatzipReviewListDTO.builder()
                    .matzipListDTO(matzipListDTO)
                    .reviewListDTOs(matchedReviews)
                    .build();

            matzipReviewList.add(matzipReviewListDTO);
        }

        return matzipReviewList;
    }
}