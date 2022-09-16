package de.ampada.tmsaddon.exception.advisor;

import de.ampada.tmsaddon.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String customExceptionHandler(CustomException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.GONE.value());
        return ex.getMessage();
    }
}