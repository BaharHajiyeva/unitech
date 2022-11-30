package com.example.unitech.advice;

import com.example.unitech.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public @ResponseBody
    ResponseEntity<?> handleException(final CustomException exception, WebRequest request) {

        log.error("CustomException -> ExceptionHandler -> {} {}", exception, request);
        ErrorDetail detail = new ErrorDetail(LocalDateTime.now(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                request.getDescription(false),
                exception.getStatus(),
                exception.getStatus().value());
        return new ResponseEntity<>(detail, exception.getStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, WebRequest request) {
        log.error("GlobalExceptionHandler -> ExceptionHandler -> {} {}", exception, request);
        exception.printStackTrace();
        ErrorDetail detail = new ErrorDetail(LocalDateTime.now(),
                exception.toString(),
                exception.getMessage(),
                request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                500);
        return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<?> handleException(org.springframework.security.access.AccessDeniedException exception, WebRequest request) {
        log.error("GlobalExceptionHandler -> ExceptionHandler -> {} {}", exception, request);
        exception.printStackTrace();
        ErrorDetail detail = new ErrorDetail(LocalDateTime.now(),
                exception.toString(),
                exception.getMessage(),
                request.getDescription(false),
                HttpStatus.FORBIDDEN,
                403);
        return new ResponseEntity<>(detail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException exception, WebRequest request) {
        log.error("GlobalExceptionHandler -> ExceptionHandler -> {} {}", exception, request);
        exception.printStackTrace();
        ErrorDetail detail = new ErrorDetail(LocalDateTime.now(),
                exception.toString(),
                exception.getMessage(),
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                400);
        return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
    }

}
