package com.bux.trading.bot.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Product {
    private String productId;
    private String positionId;
    private double price;
}
