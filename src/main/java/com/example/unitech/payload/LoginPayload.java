package com.example.unitech.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginPayload {

    @NotNull
    private String pin;
    @NotNull
    private String password;

}
