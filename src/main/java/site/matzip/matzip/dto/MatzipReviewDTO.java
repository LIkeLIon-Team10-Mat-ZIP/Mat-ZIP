package site.matzip.matzip.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import site.matzip.review.dto.ReviewCreationDTO;

@Getter
@Setter
public class MatzipReviewDTO {
    @Valid
    MatzipCreationDTO matzipCreationDTO;
    @Valid
    ReviewCreationDTO reviewCreationDTO;
}
