package de.ampada.tmsaddon.exception.advisor;

import de.ampada.tmsaddon.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseControllerAdvice {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.GONE)
    public String customExceptionHandler(CustomException ex) {
        return ex.getMessage();
    }
}