package site.matzip.review.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import site.matzip.member.domain.Member;

import java.util.List;

@Data
public class ReviewCreationDTO {
    private Member author;
    private double rating;
    private String content;
    private List<MultipartFile> imageFiles;
}