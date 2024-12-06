package br.com.migrations_flyway.exceptions.CustomizedResponse;

import br.com.migrations_flyway.exceptions.InvalidJwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.Date;

@ControllerAdvice
public class CapturedAndCustomizedExceptions {
    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<StandartError> invalidJwtAuthenticationException(Exception ex, WebRequest webRequest) {
        StandartError standartError = new StandartError(LocalDate.now(),
                ex.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(standartError, HttpStatus.FORBIDDEN);
    }
}
