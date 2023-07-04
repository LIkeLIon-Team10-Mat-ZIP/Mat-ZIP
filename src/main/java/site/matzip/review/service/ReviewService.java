package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.repository.ReviewRepository;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public Review create(ReviewCreationDTO reviewCreationDTO, Long authorId, Matzip matzip) {

        Review createdReview = Review.builder()
                .rating(reviewCreationDTO.getRating())
                .content(reviewCreationDTO.getContent())
                .build();

        Optional<Member> authorOptional = memberRepository.findById(authorId);
        Member author = authorOptional.orElseThrow(() -> new EntityNotFoundException("Member Not Found"));
        createdReview.setMatzip(matzip);
        createdReview.setAuthor(author);
        reviewRepository.save(createdReview);

        return createdReview;
    }

    public void remove(Review review) {
        reviewRepository.delete(review);
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }

    public List<Review> findByMatzipId(Long matzipId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByMatzipId(matzipId, pageable);
        return reviewPage.getContent();
    }

    public List<ReviewListDTO> findAllDto() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    public List<ReviewListDTO> findByAuthorId(Long authorId) {
        List<Review> reviews = reviewRepository.findByAuthorId(authorId);
        return reviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    private ReviewListDTO convertToReviewDTO(Review review) {
        return ReviewListDTO.builder()
                .matzipId(review.getMatzip().getId())
                .reviewId(review.getId())
                .authorNickname(review.getAuthor().getNickname())
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .build();
    }

    public void incrementViewCount(Review review) {
        review.incrementViewCount();
        reviewRepository.save(review);
    }

    public void updateViewCountWithCookie(Review review, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        // request에 쿠키가 있을 때
        for (int i = 0; cookies != null & i < cookies.length; i++) {
            if (cookies[i].getName().equals("reviewView")) {
                cookie = cookies[i];
                if (!cookie.getValue().contains("[" + review.getId() + "]")) {
                    incrementViewCount(review);
                    cookie.setValue(cookie.getValue() + "[" + review.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }

        // request에 쿠기가 없을 때
        if (!isCookie) {
            incrementViewCount(review);
            cookie = new Cookie("reviewView", "[" + review.getId() + "]");
        }

        // Cookie 유지시간 = 당일 자정까지로 설정
        ZoneId kstZoneId = ZoneId.of("Asia/Seoul");
        long todayMidnightSecond = LocalDate.now(kstZoneId).atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC) - 9 * 3600; // UTC == KST + 9h
        long currentSecond = LocalDateTime.now(kstZoneId).toEpochSecond(ZoneOffset.UTC) - 9 * 3600;

        cookie.setPath("/");
        cookie.setMaxAge((int) (todayMidnightSecond - currentSecond));
        response.addCookie(cookie);
    }

    public int getViewCount(Long reviewId) {
        Review review = findById(reviewId);
        return review.getViews();
    }
}