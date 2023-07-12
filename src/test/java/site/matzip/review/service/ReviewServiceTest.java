package site.matzip.review.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.domain.MatzipType;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.domain.Member;
import site.matzip.member.service.MemberService;
import site.matzip.review.domain.Review;
import site.matzip.review.dto.ReviewCreationDTO;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReviewServiceTest {
    private Member testUser;
    private Matzip matzip;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MatzipService matzipService;

    @BeforeEach
    public void setUp() {
        // Member 생성
        testUser = memberService.signUp("testUser",
                "testUser", "1234", "test@email.com");

        // Matzip 생성
        MatzipCreationDTO matzipCreationDTO = MatzipCreationDTO.builder()
                .address("서울 용산구 이태원동 258-7")
                .kakaoId(7849814L)
                .matzipName("라쿠치나")
                .matzipType(String.valueOf(MatzipType.KOREAN))
                .phoneNumber("02-794-6006")
                .x(126.99647354065)
                .y(37.5405570353927)
                .build();

        matzip = matzipService.create(matzipCreationDTO, testUser.getId());
    }

    @Test
    void create() {
        ReviewCreationDTO reviewCreationDTO = ReviewCreationDTO.builder()
                .rating(4.0)
                .content("맛있어요")
                .build();

        reviewService.create(reviewCreationDTO, testUser.getId(), matzip);

        List<Review> reviews = reviewService.findAll();
        Review fristReview = reviews.get(0);

        assertThat(fristReview.getContent()).isEqualTo("맛있어요");
        assertThat(fristReview.getAuthor()).isEqualTo(testUser);
    }

    @Test
    void remove() {
        ReviewCreationDTO reviewCreationDTO = ReviewCreationDTO.builder()
                .rating(4.0)
                .content("맛있어요")
                .build();

        Review review = reviewService.create(reviewCreationDTO, testUser.getId(), matzip);

        reviewService.remove(review);

        List<Review> reviews = reviewService.findAll();

        System.out.println("reviews.get(0).getContent() = " + reviews.get(0).getContent());

        assertThat(reviews.size()).isEqualTo(0);
    }
}