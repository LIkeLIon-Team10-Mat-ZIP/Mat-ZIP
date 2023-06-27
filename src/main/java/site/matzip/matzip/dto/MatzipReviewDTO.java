package site.matzip.matzip.dto;

import lombok.Data;
import site.matzip.review.dto.ReviewCreationDTO;

@Data
public class MatzipReviewDTO {
    MatzipCreationDTO matzipCreationDTO;
    ReviewCreationDTO reviewCreationDTO;

}
