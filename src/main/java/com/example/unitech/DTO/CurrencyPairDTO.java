package com.example.unitech.DTO;

import lombok.Data;

@Data
public class CurrencyPairDTO {

    private String base_code;
    private String target_code;
    private double conversion_rate;
    private double conversion_result;
}
