package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.dto.websockets.WsResponse;
import com.bux.trading.bot.rules.Rules;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradingQuoteHandlerTest {

    @InjectMocks
    private TradingQuoteHandler tradingQuoteHandler = new TradingQuoteHandler();

    @Mock
    private ProductContext productContext;

    @Mock
    private List<Rules> rules;

    @Test
    public void handleTradingQuote() {
        WsQuote tradingQuote = new WsQuote("prodId", 12345D);
        WsResponse webSocketResponseDto = new WsResponse("trading.quote", tradingQuote);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        when(productContext.getProductId()).thenReturn("prodId");

        tradingQuoteHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(rules, times(1)).parallelStream();
    }

    @Test
    public void handleTradingQuoteDifferentProductId() {
        WsQuote tradingQuote = new WsQuote("oldProdId", 12345D);
        WsResponse webSocketResponseDto = new WsResponse("trading.quote", tradingQuote);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        when(productContext.getProductId()).thenReturn("newProdId");

        tradingQuoteHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(rules, times(0)).parallelStream();
    }

    @Test
    public void handleTradingQuoteDifferentType() {
        WsQuote tradingQuote = new WsQuote("oldProdId", 12345D);
        WsResponse webSocketResponseDto = new WsResponse("newType", tradingQuote);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        tradingQuoteHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(productContext, times(0)).getProductId();
        verify(rules, times(0)).parallelStream();
    }

    @Test
    public void handleTradingQuoteNoType() {
        WsQuote tradingQuote = new WsQuote("oldProdId", 12345D);
        WsResponse webSocketResponseDto = new WsResponse(null, tradingQuote);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        tradingQuoteHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(productContext, times(0)).getProductId();
        verify(rules, times(0)).parallelStream();
    }
}