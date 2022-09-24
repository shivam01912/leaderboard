package com.intuit.leaderboard.test.infra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {LowScoreException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String responseBody = "Exception : " + ex.getMessage();
        return handleExceptionInternal(ex, responseBody,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
