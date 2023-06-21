package site.matzip.matzip.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import site.matzip.matzip.domain.MatzipType;

import java.time.LocalTime;
@Data
@Builder
public class MatzipListDTO {
    private String matzipName;
    private String description;
    private String address;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private MatzipType matzipType;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
