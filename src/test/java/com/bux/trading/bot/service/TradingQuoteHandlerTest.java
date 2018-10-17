package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.dto.websockets.WsResponse;
import com.bux.trading.bot.rules.Rules;
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

        when(productContext.getProductId()).thenReturn("prodId");

        tradingQuoteHandler.handleTradingQuote(webSocketResponseDto);

        verify(rules, times(1)).parallelStream();
    }

    @Test
    public void handleTradingQuoteDifferentProductId() {
        WsQuote tradingQuote = new WsQuote("oldProdId", 12345D);
        WsResponse webSocketResponseDto = new WsResponse("trading.quote", tradingQuote);

        when(productContext.getProductId()).thenReturn("newProdId");

        tradingQuoteHandler.handleTradingQuote(webSocketResponseDto);

        verify(rules, times(0)).parallelStream();
    }
}