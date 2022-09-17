package de.ampada.tmsaddon.exception.advisor;

import com.google.common.base.Strings;
import de.ampada.tmsaddon.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class ExceptionRestControllerAdvice {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(value = HttpStatus.GONE)
    public String customExceptionHandler(CustomException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        if (!Strings.isNullOrEmpty(ex.getFieldError().getDefaultMessage()))
            return messageSource.getMessage(ex.getFieldError().getDefaultMessage(), null, Locale.US);
        return ex.getMessage();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        if (!Strings.isNullOrEmpty(ex.getParameterName()))
            return messageSource.getMessage(ex.getParameterName(), null, Locale.US);
        return ex.getMessage();
    }

}