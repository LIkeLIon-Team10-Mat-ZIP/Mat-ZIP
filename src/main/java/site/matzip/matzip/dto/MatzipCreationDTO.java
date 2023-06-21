package site.matzip.matzip.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import site.matzip.matzip.domain.MatzipType;

import java.time.LocalTime;

@Data
public class MatzipCreationDTO {
    @NotBlank(message = "맛집 이름을 입력해주세요.")
    private String matzipName;
    @NotBlank(message = "맛집 주소를 입력해주세요.")
    private String address;
    @NotBlank(message = "맛집의 정보를 입력해주세요")
    private String description;
    @NotBlank(message = "맛집의 유형을 입력해주세요")
    private String matzipType;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public MatzipType getMatzipTypeEnum() {
        return MatzipType.valueOf(matzipType);
    }

}
