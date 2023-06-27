package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.matzip.matzip.repository.MatzipRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MatzipRepository matzipRepository;

    public void remove(Long reviewId) {
        Review findReview = findReview(reviewId);
        reviewRepository.delete(findReview);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }
}