package com.example.unitech.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class AddAccountPayload {

    @NotNull(message = "hesab nömrəsi boş ola bilməz")
    private String accountNumber;
}
