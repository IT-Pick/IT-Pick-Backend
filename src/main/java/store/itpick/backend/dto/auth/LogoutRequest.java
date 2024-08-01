package store.itpick.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogoutRequest {

    @NotBlank(message = "refreshToken: {NotBlank}")
    private String refreshToken;
}
