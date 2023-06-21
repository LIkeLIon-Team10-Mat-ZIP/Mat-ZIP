package site.matzip.comment.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.matzip.article.domain.Article;
import site.matzip.comment.domain.Comment;
import site.matzip.comment.domain.CommentRepository;
import site.matzip.commentImage.domain.CommentImage;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void create(Article article, List<CommentImage> commentImages, String title, String content) {
        Comment comment = Comment.builder()
                .article(article)
                .title(title)
                .content(content)
                .commentImages(commentImages)
                .build();

        commentRepository.save(comment);
    }

    public void remove(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        commentRepository.delete(comment);
    }
}