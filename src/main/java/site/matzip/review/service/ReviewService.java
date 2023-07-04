package site.matzip.review.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import site.matzip.review.repository.ReviewRepository;

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

        String profileImageUrl = AppConfig.getDefaultProfileImageUrl();
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

        String profileImageUrl = AppConfig.getDefaultProfileImageUrl();
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
                .build();
    }

    public List<CommentInfoDTO> convertToCommentInfoDTOS(List<Comment> comments, Long authorId) {

        String profileImageUrl = AppConfig.getDefaultProfileImageUrl();

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
}