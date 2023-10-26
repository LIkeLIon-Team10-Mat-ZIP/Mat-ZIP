package site.matzip.matzip.dto;

import lombok.*;
import site.matzip.review.dto.ReviewListDTO;

import java.util.List;

@Getter
@Builder
public class MatzipReviewListDTO {

    private MatzipListDTO matzipListDTO;
    private List<ReviewListDTO> reviewListDTOs;
}
