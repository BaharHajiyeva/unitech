package com.example.unitech.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class OtherHttpException extends CustomException{
    public OtherHttpException(String message, int httpStatusCode) {
        super(message, HttpStatus.valueOf(httpStatusCode));
    }

    public OtherHttpException(String message, int httpStatusCode, Map<String, String> params) {
        super(message, HttpStatus.valueOf(httpStatusCode), params);
    }
}
