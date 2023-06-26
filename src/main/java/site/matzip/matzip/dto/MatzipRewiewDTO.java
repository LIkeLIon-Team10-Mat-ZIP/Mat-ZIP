package site.matzip.matzip.dto;

import lombok.Data;
import site.matzip.review.dto.ReviewCreationDTO;

@Data
public class MatzipRewiewDTO {
    MatzipCreationDTO matzipCreationDTO;
    ReviewCreationDTO reviewCreationDTO;

}
