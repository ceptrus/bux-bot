package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class Amount {
    private String currency;
    private Integer decimals;
    private BigDecimal amount;
}
