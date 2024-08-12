package store.itpick.backend.common.exception;

import lombok.Getter;
import store.itpick.backend.common.response.status.ResponseStatus;

@Getter //사용자 정의 예외
public class AuthException extends RuntimeException{
    private final ResponseStatus exceptionStatus; //예외 상태 저장

    //객체를 매개변수로 예외 객체 생성
    public AuthException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    //객체, 메세지를 매개변수로 예외 객체 생성
    public AuthException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }

}
