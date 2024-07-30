package store.itpick.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private long userId;
    private JwtDTO jwt;

}
