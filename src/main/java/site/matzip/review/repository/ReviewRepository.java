package site.matzip.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.matzip.member.domain.Member;
import site.matzip.review.domain.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByMatzipId(Long matzipId, Pageable pageble);

    List<Review> findByMatzipId(Long matzipId);

    List<Review> findByAuthorId(Long authorId);

    List<Review> findByAuthor(Member author);

    @Query("SELECT r FROM Review r JOIN FETCH r.reviewImages WHERE r.id = :reviewId")
    Optional<Review> findByIdFetch(@Param("reviewId") Long reviewId);

    /*
       @EntityGraph(attributePaths = {"author"})
       설명 첨부 : https://www.notion.so/Error-463876d369544503b8457bd4f6363c64
       --------------------------------------------------------------------------------------
       @ManyToOne(fetch = FetchType.LAZY) -> FetchType.EAGER "author" 연관 필드를 즉시 로딩으로 처리"
       private Member author;             -> Review 와 연관된 Member 엔티티가 함께 로드
     */
    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT r from Review r WHERE r.createDate < :olderThanTime")
    List<Review> findReviewsOlderThan(@Param("olderThanTime") LocalDateTime olderThanTime);

    Page<Review> findByMatzipIdAndAuthorId(Long matzipId, Long authorId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.hearts WHERE r.author = :author")
    List<Review> findByAuthorForHeart(@Param("author") Member author);

}
