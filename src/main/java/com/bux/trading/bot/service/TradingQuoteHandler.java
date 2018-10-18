package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.rules.Rules;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.List;

@Slf4j
@Service
public class TradingQuoteHandler implements WsMessageHandler {
    private static final String TRADING_QUOTE = "trading.quote";

    @Autowired
    private ProductContext productContext;

    @Autowired
    private List<Rules> rules;

    private Gson gson = new Gson();

    public void handleMessage(JsonObject jsonObject, Session session) {
        JsonElement jsonElement = jsonObject.get("t");
        if (jsonElement == null) {
            return;
        }

        if (!TRADING_QUOTE.equals(jsonElement.getAsString())) {
            return;
        }

        WsQuote tradingQuote = gson.fromJson(jsonObject.get("body"), WsQuote.class);

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
