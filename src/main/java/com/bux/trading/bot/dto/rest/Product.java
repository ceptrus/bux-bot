package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
    private String securityId;
    private String symbol;
    private String displayName;
}
