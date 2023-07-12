package site.matzip.matzip.dto;

import lombok.Builder;
import lombok.Getter;
import site.matzip.review.dto.ReviewListDTO;

import java.util.List;

@Getter
@Builder
public class MatzipReviewListDTO {
    private MatzipListDTO matzipListDTO;
    private List<ReviewListDTO> reviewListDTOs;
}
