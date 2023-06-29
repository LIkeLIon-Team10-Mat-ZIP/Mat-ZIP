package site.matzip.review.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewCreationDTO {
    private double rating;
    private String content;
    private List<MultipartFile> imageFiles;
}