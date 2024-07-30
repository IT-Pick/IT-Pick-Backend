package store.itpick.backend.common.exception_handler;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.itpick.backend.common.exception.UserException;
import store.itpick.backend.common.response.BaseErrorResponse;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_VALUE;

@Slf4j
@Priority(0)
@RestControllerAdvice //모든 컨트롤러에서 발생하는 예외를 전역적으로 처리
public class UserExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) //해당 메서드의 예외 발생시 보낼
    @ExceptionHandler(UserException.class)
    public BaseErrorResponse handle_UserException(UserException e){
        log.error("[handle_UserException]", e);
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }
}
