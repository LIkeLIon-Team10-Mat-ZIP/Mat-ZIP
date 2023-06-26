package site.matzip.comment.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.comment.service.CommentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("create() Test")
    void createTest() {

    }
}