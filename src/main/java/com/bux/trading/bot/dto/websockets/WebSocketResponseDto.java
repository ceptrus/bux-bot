package com.bux.trading.bot.dto.websockets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketResponseDto {
    public static final String TRADING_QUOTE = "trading.quote";

    private String t;
    private TradingQuote body;
}
