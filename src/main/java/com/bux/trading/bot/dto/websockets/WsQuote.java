package com.bux.trading.bot.dto.websockets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WsQuote {
    private String securityId;
    private double currentPrice;
}
