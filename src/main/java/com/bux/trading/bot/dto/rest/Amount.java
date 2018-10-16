package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Amount {
    private String currency;
    private Integer decimals;
    private double amount;
}
