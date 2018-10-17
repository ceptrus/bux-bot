package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.dto.websockets.WsResponse;
import com.bux.trading.bot.rules.Rules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TradingQuoteHandler {

    @Autowired
    private ProductContext productContext;

    @Autowired
    private List<Rules> rules;

    public void handleTradingQuote(WsResponse responseDto) {
        WsQuote tradingQuote = responseDto.getBody();

        if (!tradingQuote.getSecurityId().equals(productContext.getProductId())) {
            log.warn("New quote doesn't match configured product");
            return;
        }

        log.info(String.format("New quote for product %s @ %.2f", tradingQuote.getSecurityId(), tradingQuote.getCurrentPrice()));

        rules.parallelStream()
                .filter(rule -> rule.isActive(tradingQuote))
                .forEach(rule -> rule.apply(tradingQuote));
    }
}
