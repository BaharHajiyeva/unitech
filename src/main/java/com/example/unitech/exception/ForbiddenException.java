package com.example.unitech.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ForbiddenException extends CustomException{

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, Map<String, String> params) {
        super(message, HttpStatus.FORBIDDEN, params);
    }

}
