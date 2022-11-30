package com.example.unitech.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ConflictException extends CustomException{

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, Map<String, String> params) {
        super(message, HttpStatus.CONFLICT, params);
    }

}