package site.matzip.matzip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.base.rsData.RsData;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipRecommendation;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.repository.MatzipRecommendationRepository;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.repository.ReviewRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MatzipRecommendationRepository matzipRecommendationRepository;
    private final ReviewRepository reviewRepository;

    //맛집 생성 메서드
    public RsData<Matzip> create(MatzipCreationDTO creationDTO, Member author) {
        Matzip matzip = createMatzipEntity(creationDTO);
        Matzip savedMatzip = matzipRepository.save(matzip);
        MatzipRecommendation matzipRecommendation = createMatzipRecommendationEntity(creationDTO, savedMatzip, author);
        matzipRecommendationRepository.save(matzipRecommendation);
        return RsData.of("S-1", "맛집이 등록 되었습니다.", savedMatzip);
    }
    //오버로딩: 리뷰 같이 등록시에 리뷰 DTO까지 매개변수로 포함
    public RsData<Matzip> create(MatzipCreationDTO creationDTO, ReviewCreationDTO reviewCreationDTO, Member author) {
        Matzip matzip = createMatzipEntity(creationDTO);
        Matzip savedMatzip = matzipRepository.save(matzip);
        MatzipRecommendation matzipRecommendation = createMatzipRecommendationEntity(creationDTO, savedMatzip, author);
        matzipRecommendationRepository.save(matzipRecommendation);
        Review review = createReviewEntity(savedMatzip, reviewCreationDTO, author);
        reviewRepository.save(review);
        return RsData.of("S-1", "맛집이 등록 되었습니다.", savedMatzip);
    }

    private Matzip createMatzipEntity(MatzipCreationDTO creationDTO) {
        return Matzip.builder()
                .matzipName(creationDTO.getMatzipName())
                .address(creationDTO.getAddress())
                .matzipType(creationDTO.getMatzipTypeEnum())
                .phoneNumber(creationDTO.getPhoneNumber())
                .matzipUrl(creationDTO.getMatzipUrl())
                .x(creationDTO.getX())
                .y(creationDTO.getY())
                .build();
    }

    private MatzipRecommendation createMatzipRecommendationEntity(MatzipCreationDTO creationDTO, Matzip savedMatzip, Member author) {
        return MatzipRecommendation.builder()
                .rating(creationDTO.getRating())
                .description(creationDTO.getDescription())
                .matzip(savedMatzip)
                .author(author)
                .build();
    }

    private Review createReviewEntity(Matzip savedMatzip, ReviewCreationDTO reviewCreationDTO, Member author) {
        return Review.builder()
                .matzip(savedMatzip)
                .content(reviewCreationDTO.getContent())
                .rating(reviewCreationDTO.getRating())
                .author(author)
                .build();
    }

    public List<Matzip> findAllWithRecommendations() {
        return matzipRepository.findAllWithRecommendations();
    }

    public List<Matzip> findAll() {
        return matzipRepository.findAll();
    }
}
