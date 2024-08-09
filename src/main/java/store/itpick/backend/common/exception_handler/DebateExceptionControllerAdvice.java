package store.itpick.backend.common.exception_handler;

import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.itpick.backend.common.exception.DebateException;
import store.itpick.backend.common.response.BaseErrorResponse;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class DebateExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DebateException.class)
    public BaseErrorResponse handleDebateException(DebateException e) {
        log.error("[DebateException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }
}
