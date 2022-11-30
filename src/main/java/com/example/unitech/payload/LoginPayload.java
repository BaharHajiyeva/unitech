package com.example.unitech.payload;

import lombok.Data;

@Data
public class LoginPayload {

    private String pin;
    private String password;

}
