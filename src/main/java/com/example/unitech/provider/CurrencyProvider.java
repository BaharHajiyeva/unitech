package com.example.unitech.provider;

import com.example.unitech.DTO.CurrencyPairDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrencyProvider {

    @Value("${currency.pair.url}")
    String currencyPairUrl;

    private final RestTemplate restTemplate;

    public CurrencyPairDTO getCurrencyPairs(String from,String to, String amount){
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(currencyPairUrl);

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("from", from);
        urlParams.put("to", to);
        urlParams.put("amount",amount);

        CurrencyPairDTO pairDTO = null;
        try {
            pairDTO = restTemplate.exchange(
                    builder.buildAndExpand(urlParams).toUri(),
                    HttpMethod.GET,
                    new HttpEntity<>(createHeader()),
                    CurrencyPairDTO.class
            ).getBody();
            log.info("In CurrencyProvider.getCurrencyPairs() - pairDTO successfully exchanged from {} ", builder.buildAndExpand(urlParams).toUri());
        }catch (Exception e){
            log.error("In CurrencyProvider.getCurrencyPairs() - data couldnt exchanged! Error: {} ", e.getMessage());
        }

        return pairDTO;
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","*/*");
        return headers;
    }




}
