package store.itpick.backend.common.exception;

import lombok.Getter;
import store.itpick.backend.common.response.status.ResponseStatus;

@Getter
public class VoteException extends RuntimeException {
    private final ResponseStatus exceptionStatus;
    public VoteException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    //객체, 메세지를 매개변수로 예외 객체 생성
    public VoteException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }


}
