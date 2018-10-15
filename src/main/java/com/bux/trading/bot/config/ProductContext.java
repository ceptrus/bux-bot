package com.bux.trading.bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ProductContext {

    @Value("${trading.product.id}")
    private String productId;

    @Value("${trading.product.buying.price}")
    private double buyingPrice;

    @Value("${trading.product.upper.limit.price}")
    private double upperLimitPrice;

    @Value("${trading.product.lower.limit.price}")
    private double lowerLimitPrice;
}
