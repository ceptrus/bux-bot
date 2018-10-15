package com.bux.trading.bot.dto.rest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestOrder {
    private String productId;
    private Amount investingAmount;
    private Integer leverage;
    private TradeType direction;
    private Source source;
}
