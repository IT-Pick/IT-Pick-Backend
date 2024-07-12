package store.itpick.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "email: {NotBlank}")
    private String email;

    @NotBlank(message = "password: {NotBlank}")
    private String password;

}
