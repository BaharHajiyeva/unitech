package com.example.unitech.DTO;

import lombok.Data;

@Data
public class AccountDTO {

    private String accountNumber;
    private boolean active;
    private Double balance;
    private UserDTO user;
}
