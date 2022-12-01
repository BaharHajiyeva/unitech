package com.example.unitech.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountToAccountPayload {

    @NotNull(message = "haradan xanası boş ola bilməz")
    private String fromAccountNumber;
    @NotNull(message = "haraya xanası boş ola bilməz")
    private String toAccountNumber;
    @NotNull(message = "məbləğ xanası boş ola bilməz")
    private double amount;

}
