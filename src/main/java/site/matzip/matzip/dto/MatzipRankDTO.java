package site.matzip.matzip.dto;

import lombok.*;

@Getter
@Builder
public class MatzipRankDTO {

    private String matzipName;
    private double averageRating;
    private long userCount;
    private long reviewCount;
    private String matzipUrl;
}