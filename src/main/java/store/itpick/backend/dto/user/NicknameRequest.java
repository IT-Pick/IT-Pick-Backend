package store.itpick.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class NicknameRequest {
    @NotBlank(message = "nickname: 필수입니다.")
    @Length(max = 25, message = "nickname: 최대 {max}자리까지 가능합니다")
    private String nickname;
}
