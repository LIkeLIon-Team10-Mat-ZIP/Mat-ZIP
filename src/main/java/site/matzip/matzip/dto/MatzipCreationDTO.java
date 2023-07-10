package site.matzip.matzip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import site.matzip.matzip.domain.MatzipType;

@Data
public class MatzipCreationDTO {
    @NotBlank(message = "맛집 이름을 입력해주세요.")
    private String matzipName;
    @NotBlank(message = "맛집 주소를 입력해주세요.")
    private String address;
    @Pattern(regexp = "^\\(?(\\d{2,3})\\)?[- ]?(\\d{3,4})[- ]?(\\d{4})$",
            message = "전화번호 형식이 잘못되었습니다. (올바른 형식: 010-1234-5678 or 02-123-4567)")
    private String phoneNumber;
    @NotBlank(message = "맛집의 URL을 입력해주세요")
    private String matzipUrl;
    @NotBlank(message = "Kakao Map API의 ID를 입력해주세요")
    private Long kakaoId;
    @NotBlank(message = "맛집의 유형을 입력해주세요")
    private String matzipType;
    private String description;
    @NotBlank(message = "맛집점수를 입력해주세요")
    private double rating;
    @NotBlank(message = "맛집의 X좌표를 입력해주세요")
    private double x;
    @NotBlank(message = "맛집의 Y좌표를 입력해주세요")
    private double y;

    public MatzipType getMatzipTypeEnum() {
        return MatzipType.valueOf(matzipType);
    }

}
