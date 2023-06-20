package site.matzip.review.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.matzip.article.domain.Article;
import site.matzip.review.domain.Review;
import site.matzip.review.domain.ReviewRepository;
import site.matzip.reviewImage.domain.ReviewImage;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void create(Article article, List<ReviewImage> reviewImages, String title, String content) {
        Review review = Review.builder()
                .article(article)
                .title(title)
                .content(content)
                .reviewImages(reviewImages)
                .build();

        reviewRepository.save(review);
    }

    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewRepository.delete(review);
    }
}