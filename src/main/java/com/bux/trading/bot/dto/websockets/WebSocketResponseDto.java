package com.bux.trading.bot.dto.websockets;

public class WebSocketResponseDto {
    public static final String TRADING_QUOTE = "trading.quote";

    private String t;
    private TradingQuote body;

    private WebSocketResponseDto() {}

    public String getT() {
        return t;
    }

    public TradingQuote getBody() {
        return body;
    }
}
