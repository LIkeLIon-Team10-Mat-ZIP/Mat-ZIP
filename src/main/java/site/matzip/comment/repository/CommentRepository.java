package site.matzip.comment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.matzip.comment.domain.Comment;
import site.matzip.member.domain.Member;

import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthor(Member author);

    /*
      @EntityGraph(attributePaths = {"author"})
      설명 첨부 : https://www.notion.so/Error-463876d369544503b8457bd4f6363c64
      --------------------------------------------------------------------------------------
      @ManyToOne(fetch = FetchType.LAZY) -> FetchType.EAGER "author" 연관 필드를 즉시 로딩으로 처리"
      private Member author;             -> Comment 와 연관된 Member 엔티티가 함께 로드
    */
    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT c from Comment c WHERE c.createDate < :olderThanTime")
    List<Comment> findCommentsOlderThan(@Param("olderThanTime") LocalDateTime olderThanTime);
}