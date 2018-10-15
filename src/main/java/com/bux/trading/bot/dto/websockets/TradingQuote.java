package com.bux.trading.bot.dto.websockets;

import lombok.Getter;

@Getter
public class TradingQuote {
    private String securityId;
    private double currentPrice;

    private TradingQuote() {}
}
