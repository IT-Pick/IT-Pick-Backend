package store.itpick.backend.common.exception_handler;

import jakarta.annotation.Priority;
import store.itpick.backend.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.BAD_SQL_GRAMMAR;
import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class DatabaseExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadSqlGrammarException.class)
    public BaseErrorResponse handle_BadSqlGrammarException(BadSqlGrammarException e) {
        log.error("[handle_BadSqlGrammarException]", e);
        return new BaseErrorResponse(BAD_SQL_GRAMMAR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public BaseErrorResponse handle_DataAccessException(DataAccessException e) {
        log.error("[handle_DataAccessException]", e);
        return new BaseErrorResponse(DATABASE_ERROR);
    }
}
