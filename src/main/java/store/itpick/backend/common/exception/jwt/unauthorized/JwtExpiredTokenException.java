package store.itpick.backend.common.exception.jwt.unauthorized;

import kuit3.backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtExpiredTokenException extends JwtUnauthorizedTokenException {

    private final ResponseStatus exceptionStatus;

    public JwtExpiredTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}
