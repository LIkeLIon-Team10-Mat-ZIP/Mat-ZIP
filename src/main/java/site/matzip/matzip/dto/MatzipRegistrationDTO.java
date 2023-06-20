package site.matzip.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRegistrationDTO {

    @NotBlank(message = "식당 이름을 입력해주세요.")
    private String restaurantName;

    @NotBlank(message = "식당 주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "식당의 정보를 입력해주세요")
    private String description;

}
