package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.dto.CommentInfoDTO;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewDetailDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.repository.HeartRepository;
import site.matzip.review.repository.ReviewRepository;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final AppConfig appConfig;

    @CacheEvict(value = {"reviewListCache", "myReviewListCache"}, allEntries = true)
    public Review create(ReviewCreationDTO reviewCreationDTO, Long authorId, Matzip matzip) {

        Review createdReview = Review.builder()
                .rating(reviewCreationDTO.getRating())
                .content(reviewCreationDTO.getContent())
                .build();

        Member author = memberRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Member not Found"));
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

    @Cacheable(value = "reviewListCache")
    public List<ReviewListDTO> findAndConvertAll() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "myReviewListCache")
    public List<ReviewListDTO> findAndConvertMine(Long authorId) {
        List<Review> reviews = reviewRepository.findByAuthorId(authorId);
        return reviews.stream().map(this::convertToReviewDTO).collect(Collectors.toList());
    }

    private ReviewListDTO convertToReviewDTO(Review review) {

        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (review.getAuthor().getProfileImage() != null && review.getAuthor().getProfileImage().getImageUrl() != null) {
            profileImageUrl = review.getAuthor().getProfileImage().getImageUrl();
        }

        return ReviewListDTO.builder()
                .matzipId(review.getMatzip().getId())
                .reviewId(review.getId())
                .authorNickname(review.getAuthor().getNickname())
                .profileImageUrl(profileImageUrl)
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .build();
    }

    public ReviewDetailDTO convertToReviewDetailDTO(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
        Matzip matzip = review.getMatzip();
        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (review.getAuthor().getProfileImage() != null && review.getAuthor().getProfileImage().getImageUrl() != null) {
            profileImageUrl = review.getAuthor().getProfileImage().getImageUrl();
        }

        return ReviewDetailDTO.builder()
                .profileImageUrl(profileImageUrl)
                .authorNickname(review.getAuthor().getNickname())
                .reviewId(review.getId())
                .matzipName(matzip.getMatzipName())
                .createDate(review.getCreateDate())
                .address(matzip.getAddress())
                .rating(review.getRating())
                .matzipType(matzip.getMatzipType())
                .phoneNumber(matzip.getPhoneNumber())
                .content(review.getContent())
                .heartCount(countHeart(review))
                .build();
    }

    private int countHeart(Review review) {
        return heartRepository.findByReview(review).size();
    }

    public List<CommentInfoDTO> convertToCommentInfoDTOS(List<Comment> comments, Long authorId) {

        String profileImageUrl = appConfig.getDefaultProfileImageUrl();

        return comments.stream()
                .map(comment -> CommentInfoDTO.builder()
                        .profileImageUrl(comment.getAuthor().getProfileImage() != null ? comment.getAuthor().getProfileImage().getImageUrl() : profileImageUrl)
                        .id(comment.getId())
                        .loginId(authorId)
                        .authorId(comment.getAuthor().getId())
                        .authorNickname(comment.getAuthor().getNickname())
                        .createDate(comment.getCreateDate())
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());
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

    @Scheduled(fixedRate = 10 * 60 * 1000) // 주기 10분
    public void rewardPointsForReviews() {
        LocalDateTime referenceTime = LocalDateTime.now().minusHours(appConfig.getPointRewardReferenceTime());
        List<Review> validReviews = reviewRepository.findReviewsOlderThan(referenceTime);

        for (Review review : validReviews) {
            if (!review.isPointsRewarded()) {
                Member author = review.getAuthor();
                author.addPoints(appConfig.getPointRewardReview());
                memberRepository.save(author); // 포인트 업데이트
                review.updatePointsRewarded(); // 포인트 지급 여부 업데이트
                reviewRepository.save(review); // 댓글 업데이트
            }
        }
    }

    public int getHeartCount(Long reviewId) {
        return heartRepository.findByReviewId(reviewId).size();
    }
}