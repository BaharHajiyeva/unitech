package com.example.unitech.controller;

import com.example.unitech.DTO.CurrencyPairDTO;
import com.example.unitech.provider.CurrencyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyProvider provider;

    @GetMapping("/{from}/{to}/{amount}")
    public CurrencyPairDTO getPair(@PathVariable String from,
                                   @PathVariable String to,
                                   @PathVariable String amount) {

        return provider.getCurrencyPairs(from, to, amount);
    }

}
