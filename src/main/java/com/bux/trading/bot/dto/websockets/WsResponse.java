package com.bux.trading.bot.dto.websockets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WsResponse {
    private String t;
    private WsQuote body;
}
