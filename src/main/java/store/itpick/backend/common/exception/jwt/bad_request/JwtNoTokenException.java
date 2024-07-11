package store.itpick.backend.common.exception.jwt.bad_request;

import kuit3.backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtNoTokenException extends JwtBadRequestException {

    private final ResponseStatus exceptionStatus;

    public JwtNoTokenException(ResponseStatus exceptionStatus) {
        super(exceptionStatus);
        this.exceptionStatus = exceptionStatus;
    }
}