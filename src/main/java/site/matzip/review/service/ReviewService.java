package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.base.appConfig.AppConfig;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.dto.CommentInfoDTO;
import site.matzip.config.auth.PrincipalDetails;
import site.matzip.image.domain.ReviewImage;
import site.matzip.matzip.domain.Matzip;
import site.matzip.member.domain.Member;
import site.matzip.member.repository.MemberRepository;
import site.matzip.review.domain.Heart;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;
import site.matzip.review.dto.ReviewDetailDTO;
import site.matzip.review.dto.ReviewListDTO;
import site.matzip.review.repository.HeartRepository;
import site.matzip.review.repository.ReviewRepository;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final AppConfig appConfig;

    @Transactional
    @CacheEvict(value = {"reviewListCache", "myReviewListCache"}, allEntries = true)
    public Review create(ReviewCreationDTO reviewCreationDTO, Long authorId, Matzip matzip) {

        Review createdReview = Review.builder()
                .rating(reviewCreationDTO.getRating())
                .content(reviewCreationDTO.getContent())
                .build();

        Member author = memberRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Member not Found"));
        createdReview.addAssociation(matzip, author);
        reviewRepository.save(createdReview);

        return createdReview;
    }

    @Transactional
    @CacheEvict(value = {"reviewListCache", "myReviewListCache"}, allEntries = true)
    public void remove(Review review) {
        reviewRepository.delete(review);
    }

    @Transactional
    @CacheEvict(value = {"reviewListCache", "myReviewListCache"}, allEntries = true)
    public Review modify(Review review, ReviewCreationDTO reviewCreationDTO) {
        review.updateContent(reviewCreationDTO.getContent());
        review.updateRating(reviewCreationDTO.getRating());
        reviewRepository.save(review);

        return review;
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Cacheable(value = "reviewListCache")
    public Page<ReviewListDTO> findByMatzipIdAndConvertToDTO(Long matzipId, int pageSize, int pageNumber) {
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Review> reviewPage = reviewRepository.findByMatzipId(matzipId, pageable);

        return reviewPage.map(this::convertToReviewDTO);
    }

    @Cacheable(value = "myReviewListCache", key = "T(java.util.Objects).hash(#matzipId, #authorId)")
    public Page<ReviewListDTO> findByMatzipIdWithAuthorAndConvertToReviewDTO(Long matzipId, Long authorId, int pageSize, int pageNumber) {
        Sort sort = Sort.by(Sort.Direction.DESC, "views");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Review> reviewPage = reviewRepository.findByMatzipIdAndAuthorId(matzipId, authorId, pageable);

        return reviewPage.map(this::convertToReviewDTO);
    }

    private ReviewListDTO convertToReviewDTO(Review review) {
        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (review.getAuthor().getProfileImage() != null && review.getAuthor().getProfileImage().getImageUrl() != null) {
            profileImageUrl = review.getAuthor().getProfileImage().getImageUrl();
        }

        String reviewFirstImageUrl = (review.getReviewImages().size() == 0) ? "" : review.getReviewImages().get(0).getImageUrl();

        return ReviewListDTO.builder()
                .matzipId(review.getMatzip().getId())
                .reviewId(review.getId())
                .authorId(review.getAuthor().getId())
                .authorNickname(review.getAuthor().getNickname())
                .profileImageUrl(profileImageUrl)
                .content(review.getContent())
                .rating(review.getRating())
                .createDate(review.getCreateDate())
                .matzipCount(review.getAuthor().getMatzipMembers().size())
                .reviewCount(review.getAuthor().getReviews().size())
                .friendCount(review.getAuthor().getFriends2().size())
                .reviewImageUrl(reviewFirstImageUrl)
                .reviewImageCount(review.getReviewImages().size())
                .build();
    }

    public ReviewDetailDTO convertToReviewDetailDTO(Long id, Long loginId) {
        // TODO findByIdFetch로 NotProd 클래스 삭제 후 변경
        // 쿼리 양 때문에 fetch로 변경해야 함
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
        Matzip matzip = review.getMatzip();
        String profileImageUrl = appConfig.getDefaultProfileImageUrl();
        if (review.getAuthor().getProfileImage() != null && review.getAuthor().getProfileImage().getImageUrl() != null) {
            profileImageUrl = review.getAuthor().getProfileImage().getImageUrl();
        }

        return ReviewDetailDTO.builder()
                .profileImageUrl(profileImageUrl)
                .authorNickname(review.getAuthor().getNickname())
                .matzipUrl(review.getMatzip().getMatzipUrl())
                .reviewId(review.getId())
                .authorId(review.getAuthor().getId())
                .loginId(loginId)
                .matzipName(matzip.getMatzipName())
                .createDate(review.getCreateDate())
                .address(matzip.getAddress())
                .rating(review.getRating())
                .matzipType(matzip.getMatzipType())
                .phoneNumber(matzip.getPhoneNumber())
                .content(review.getContent())
                .heartCount(countHeart(review))
                .imageUrls(review.getReviewImages()
                        .stream()
                        .map(ReviewImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    private int countHeart(Review review) {
        return heartRepository.findByReview(review).size();
    }

    @Transactional
    public List<CommentInfoDTO> convertToCommentInfoDTOS(List<Comment> comments, Long authorId) {

        String profileImageUrl = appConfig.getDefaultProfileImageUrl();

        return comments.stream().map(comment -> CommentInfoDTO.builder()
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

    @Transactional
    public void updateViewCountWithCookie(Review review, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        // request에 쿠키가 있을 때
        if (cookies != null) {
            for (Cookie value : cookies) {
                if (value.getName().equals("reviewView")) {
                    cookie = value;
                    if (!cookie.getValue().contains("[" + review.getId() + "]")) {
                        incrementViewCount(review);
                        cookie.setValue(cookie.getValue() + "[" + review.getId() + "]");
                    }
                    isCookie = true;
                    break;
                }
            }
        }

        // request에 쿠기가 없을 때
        if (cookies == null || !isCookie) {
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

    @Transactional
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

    public boolean isHeart(Member member, Review review) {
        return heartRepository.findByMemberAndReview(member, review).isPresent();
    }

    @Transactional
    public void updateHeart(Long memberId, Long reviewId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
        Review findReview = findReview(reviewId);
        Optional<Heart> findHeart = heartRepository.findByMemberAndReview(findMember, findReview);

        if (findHeart.isEmpty()) {
            Heart createdHeart = Heart.builder().build();
            createdHeart.addAssociation(findMember, findReview);
            heartRepository.save(createdHeart);
        } else {
            heartRepository.delete(findHeart.get());
        }
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not Found"));
    }

    public boolean isImageFileEmpty(ReviewCreationDTO reviewCreationDTO) {
        return reviewCreationDTO.getImageFiles().size() == 1 && reviewCreationDTO.getImageFiles().get(0).isEmpty();
    }

    public void checkAccessPermission(Long reviewId, PrincipalDetails principalDetail) {
        Review review = findById(reviewId);
        if (!Objects.equals(review.getAuthor().getId(), principalDetail.getMember().getId())) {
            throw new AccessDeniedException("You do not have permission.");
        }
    }
}