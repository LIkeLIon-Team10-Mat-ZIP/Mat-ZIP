package site.matzip.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewCreationDTO {
    @NotNull(message = "리뷰점수를 입력해주세요")
    private double rating;
    @NotBlank(message = "리뷰 내용을 입력해주세요")
    private String content;
    private List<MultipartFile> imageFiles;
    private List<String> imageUrls;
}