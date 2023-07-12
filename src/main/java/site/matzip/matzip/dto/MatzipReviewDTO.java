package site.matzip.matzip.dto;

import jakarta.validation.Valid;
import lombok.Data;
import site.matzip.review.dto.ReviewCreationDTO;

@Data
public class MatzipReviewDTO {
    @Valid
    MatzipCreationDTO matzipCreationDTO;
    @Valid
    ReviewCreationDTO reviewCreationDTO;
}
