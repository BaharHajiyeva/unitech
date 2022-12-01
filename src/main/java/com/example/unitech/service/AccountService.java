package com.example.unitech.service;

import com.example.unitech.DTO.AccountDTO;
import com.example.unitech.payload.AccountToAccountPayload;
import com.example.unitech.payload.AddAccountPayload;
import com.example.unitech.payload.TopUpPayload;

import java.util.List;

public interface AccountService {

    AccountDTO add(AddAccountPayload payload, String bearerToken);

    List<AccountDTO> getAccounts(String bearerToken);

    AccountDTO topUp(String bearerToken, TopUpPayload payload);

    AccountDTO accountToAccount(AccountToAccountPayload payload, String bearerToken);

}
