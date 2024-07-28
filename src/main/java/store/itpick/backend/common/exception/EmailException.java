package store.itpick.backend.common.exception;

import lombok.Getter;
import store.itpick.backend.common.response.status.ResponseStatus;

@Getter
public class EmailException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public EmailException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

}
