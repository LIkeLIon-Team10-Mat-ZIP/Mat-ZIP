package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MatzipRepository matzipRepository;

    @Transactional
    public void create(MatzipCreationDTO matzipCreationDTO, ReviewCreationDTO reviewCreationDTO) {
        //맛집 등록
        Matzip matzip = Matzip.builder()
                .matzipName(matzipCreationDTO.getMatzipName())
                .address(matzipCreationDTO.getAddress())
                .matzipType(matzipCreationDTO.getMatzipTypeEnum())
                .phoneNumber(matzipCreationDTO.getPhoneNumber())
                .x(matzipCreationDTO.getX())
                .y(matzipCreationDTO.getY())
                .build();
        Matzip savedMatzip = matzipRepository.save(matzip);

        //리뷰 등록
        Review review = Review.builder()
                .matzip(savedMatzip)
                .author(reviewCreationDTO.getAuthor())
                .rating(reviewCreationDTO.getRating())
                .content(reviewCreationDTO.getContent())
                .build();
        reviewRepository.save(review);
    }

    public void remove(Long reviewId) {
        Review findReview = findReview(reviewId);
        reviewRepository.delete(findReview);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }
}