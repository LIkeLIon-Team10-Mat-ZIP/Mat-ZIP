package site.matzip.matzip.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import site.matzip.matzip.domain.MatzipType;

@Getter
@Builder
public class MatzipListDTO {
    private Long matzipId;
    private String matzipName;
    private String description;
    private String address;
    private String phoneNumber;
    private String matzipUrl;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private double x;
    private double y;
    private double rating;
    private double averageRating;
}
