package store.itpick.backend.common.exception_handler;

import jakarta.annotation.Priority;
import store.itpick.backend.common.exception.jwt.bad_request.JwtBadRequestException;
import store.itpick.backend.common.exception.jwt.unauthorized.JwtUnauthorizedTokenException;
import store.itpick.backend.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class JwtExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtBadRequestException.class)
    public BaseErrorResponse handle_JwtBadRequestException(JwtBadRequestException e) {
        log.error("[handle_JwtBadRequestException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtUnauthorizedTokenException.class)
    public BaseErrorResponse handle_JwtUnauthorizedException(JwtUnauthorizedTokenException e) {
        log.error("[handle_JwtUnauthorizedException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }
}