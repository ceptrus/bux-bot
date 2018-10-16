package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Product {
    private String securityId;
    private String symbol;
    private String displayName;
}
