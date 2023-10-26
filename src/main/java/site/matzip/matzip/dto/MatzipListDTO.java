package site.matzip.matzip.dto;

import jakarta.persistence.*;
import lombok.*;
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
