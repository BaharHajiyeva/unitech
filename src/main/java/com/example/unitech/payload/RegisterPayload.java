package com.example.unitech.payload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RegisterPayload {

    @NotNull(message = "Pin boş ola bilməz")
    private String pin;
    @NotNull(message = "Parol boş ola bilməz")
    private String password;
    @NotNull(message = "Ad boş ola bilməz")
    private String name;
    @NotNull(message = "Soyad boş ola bilməz")
    private String surname;

}
