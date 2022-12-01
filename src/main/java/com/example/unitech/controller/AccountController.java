package com.example.unitech.controller;

import com.example.unitech.DTO.AccountDTO;
import com.example.unitech.payload.AccountToAccountPayload;
import com.example.unitech.payload.AddAccountPayload;
import com.example.unitech.payload.TopUpPayload;
import com.example.unitech.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // İstifadəçi öz hesabına bank kartı əlavə edir / Users add bank account to their accounts
    @PostMapping
    public ResponseEntity<AccountDTO> addAccountToUSer(@RequestBody @Valid AddAccountPayload payload,
                                                       @RequestHeader(value = "Authorization") String bearerToken){

        return ResponseEntity.ok(accountService.add(payload,bearerToken));
    }

    // İstifadəçi öz aktiv bank hesablarını görür /  User gets her active bank accounts
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts(@RequestHeader(value = "Authorization") String bearerToken){

        return ResponseEntity.ok(accountService.getAccounts(bearerToken));
    }

    // balansı artırmaq / top-up
    @PostMapping("/top-up")
    public ResponseEntity<AccountDTO> topUp(@RequestBody @Valid TopUpPayload payload,
            @RequestHeader(value = "Authorization") String bearerToken){

        return ResponseEntity.ok(accountService.topUp(bearerToken,payload));
    }

    // hesabdan hesaba köçürmə / account to account
    @PostMapping("/account-to-account")
    public ResponseEntity<AccountDTO> accountToAccount(@RequestBody @Valid AccountToAccountPayload payload,
                                                       @RequestHeader(value = "Authorization") String bearerToken){

        return ResponseEntity.ok(accountService.accountToAccount(payload, bearerToken));
    }

}
