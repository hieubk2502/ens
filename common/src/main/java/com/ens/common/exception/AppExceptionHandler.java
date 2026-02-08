package com.ens.common.exception;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    private final MessageSource messageSource;

    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handle(AppException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getArgs(),
                ex.getMessageKey(),
                locale
        );
        ErrorResponse response = new ErrorResponse(ex.getMessageKey(), message);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    public record ErrorResponse(String code, String message) {}
}
