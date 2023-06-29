package site.matzip.matzip.dto;

import lombok.Builder;
import lombok.Data;
import site.matzip.review.dto.ReviewListDTO;

import java.util.List;

@Data
@Builder
public class MatzipReviewListDTO {
    private MatzipListDTO matzipListDTO;
    private List<ReviewListDTO> reviewListDTOs;
}
