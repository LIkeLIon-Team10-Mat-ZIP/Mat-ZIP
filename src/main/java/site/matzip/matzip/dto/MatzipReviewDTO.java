package site.matzip.matzip.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import site.matzip.review.dto.ReviewCreationDTO;

@Getter
public class MatzipReviewDTO {
    @Valid
    MatzipCreationDTO matzipCreationDTO;
    @Valid
    ReviewCreationDTO reviewCreationDTO;
}
