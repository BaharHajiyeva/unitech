package com.example.unitech.payload;

import lombok.Data;

@Data
public class AccountToAccountPayload {

    private String fromAccountNumber;
    private String toAccountNumber;
    private double amount;

}
