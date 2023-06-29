package site.matzip.matzip.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import site.matzip.matzip.domain.MatzipType;

@Data
@Builder
public class MatzipListDTO {
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
    private Long id;
}
