package site.matzip.matzip.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import site.matzip.matzip.domain.MatzipType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatzipCreationDTO {

    @NotBlank(message = "맛집 이름을 입력해주세요.")
    private String matzipName;
    @NotBlank(message = "맛집 주소를 입력해주세요.")
    private String address;
    private String phoneNumber;
    @NotBlank(message = "맛집의 URL을 입력해주세요")
    private String matzipUrl;
    private Long kakaoId;
    @NotBlank(message = "맛집의 유형을 입력해주세요")
    private String matzipType;
    private String description;
    private double rating;
    private double x;
    private double y;

    public MatzipType getMatzipTypeEnum() {
        return MatzipType.valueOf(matzipType);
    }

}
