package store.itpick.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class PasswordRequest {
    @NotBlank(message = "password: {NotBlank}")
    @Length(min = 8, max = 20,
            message = "password: 최소 {min}자리 ~ 최대 {max}자리까지 가능합니다")
    @Pattern(regexp = "(?=.*[a-z])(?=.*[0-9])(?=.*[!#@$%^&*+=])(?=\\S+$).{8,20}",
            message = "password: 소문자, 숫자, 특수문자가 적어도 하나씩은 있어야 합니다")
    private String password;
}
