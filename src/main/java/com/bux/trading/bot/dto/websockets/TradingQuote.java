package com.bux.trading.bot.dto.websockets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradingQuote {
    private String securityId;
    private double currentPrice;
}
