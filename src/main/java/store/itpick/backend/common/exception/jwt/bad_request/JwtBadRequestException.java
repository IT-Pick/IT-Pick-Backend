package store.itpick.backend.common.exception.jwt.bad_request;

import kuit3.backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class JwtBadRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public JwtBadRequestException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}