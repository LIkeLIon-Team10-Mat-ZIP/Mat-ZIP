package site.matzip.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameUpdateDTO {
    @NotBlank(message = "변경할 닉네임을 입력해주세요.")
    @Size(min = 2, max = 15)
    private String nickname;
}
