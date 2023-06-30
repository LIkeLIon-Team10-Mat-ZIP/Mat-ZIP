package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review create(ReviewCreationDTO reviewCreationDTO, Member author, Matzip matzip) {
        Review createdReview = Review.builder()
                .rating(reviewCreationDTO.getRating())
                .content(reviewCreationDTO.getContent())
                .build();
        createdReview.setMatzip(matzip);
        createdReview.setAuthor(author);
        reviewRepository.save(createdReview);

        return createdReview;
    }

    public void remove(Review review) {
        reviewRepository.delete(review);
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }

    public List<Review> findByMatzipId(Long matzipId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByMatzipId(matzipId, pageable);
        return reviewPage.getContent();
    }

    public List<ReviewListDTO> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    private ReviewListDTO convertToReviewDTO(Review review) {
        return ReviewListDTO.builder()
                .matzipId(review.getMatzip().getId())
                .authorNickname(review.getAuthor().getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .build();
    }
}