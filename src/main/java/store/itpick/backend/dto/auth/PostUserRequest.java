package store.itpick.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUserRequest {

    @Email(message = "email: 이메일 형식이어야 합니다")
    @NotBlank(message = "email: {NotBlank}")
    @Length(max = 50, message = "email: 최대 {max}자리까지 가능합니다")
    private String email;


    @NotBlank(message = "password: {NotBlank}")
    @Length(min = 8, max = 20,
            message = "password: 최소 {min}자리 ~ 최대 {max}자리까지 가능합니다")
    @Pattern(regexp = "(?=.*[a-z])(?=.*[0-9])(?=.*[!#@$%^&*+=])(?=\\S+$).{8,20}",
            message = "password: 소문자, 숫자, 특수문자가 적어도 하나씩은 있어야 합니다")
    private String password;

    @NotBlank(message = "nickname: 필수입니다.")
    @Length(max = 25, message = "nickname: 최대 {max}자리까지 가능합니다")
    private String nickname;


    @NotBlank(message = "birth_date: 필수입니다.")
    @Length(min = 6, max = 6, message = "birth_date: 정확히 {max}자리여야 합니다")
    private String birth_date;

    private List<String> likedTopics;

}
