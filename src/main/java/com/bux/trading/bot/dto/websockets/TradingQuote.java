package com.bux.trading.bot.dto.websockets;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TradingQuote {
    private String securityId;
    private double currentPrice;
}
