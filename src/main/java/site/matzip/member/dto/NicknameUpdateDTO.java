package site.matzip.member.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class NicknameUpdateDTO {

    @NotBlank(message = "변경할 닉네임을 입력해주세요.")
    @Size(min = 2, max = 15)
    private String nickname;
}
