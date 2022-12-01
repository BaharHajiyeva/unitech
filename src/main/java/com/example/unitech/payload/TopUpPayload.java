package com.example.unitech.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TopUpPayload {

    @NotNull(message = "hesab nömrəsi boş ola bilməz")
    private String accountNumber;
    @NotNull(message = "məbləğ boş ola bilməz")
    private double amount;

}
