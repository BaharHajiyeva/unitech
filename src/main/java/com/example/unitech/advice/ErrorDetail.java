package com.example.unitech.advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorDetail {

    private LocalDateTime timestamp;
    private String exception;
    private String message;
    private String path;
    private HttpStatus status;
    private int code;

    public ErrorDetail(LocalDateTime timestamp, String exception, String message, String path, HttpStatus status, int code) {
        this.timestamp = timestamp;
        this.exception = exception;
        this.message = message;
        this.path = path;
        this.status = status;
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
