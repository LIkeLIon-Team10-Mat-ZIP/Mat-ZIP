package site.matzip.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import site.matzip.badge.domain.MemberBadge;
import site.matzip.member.domain.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.matzip.review.domain.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByMatzipId(Long matzipId, Pageable pageble);

    List<Review> findByMatzipId(Long matzipId);

    List<Review> findByAuthorId(Long authorId);

    List<Review> findByAuthor(Member author);

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

    @Query("SELECT m FROM Review m JOIN FETCH m.hearts WHERE m.author = :author")
    List<Review> findByAuthorForHeart(@Param("author") Member author);
}
