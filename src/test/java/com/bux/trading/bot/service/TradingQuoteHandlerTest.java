package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.dto.websockets.WebSocketResponseDto;
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
        TradingQuote tradingQuote = new TradingQuote("prodId", 12345D);
        WebSocketResponseDto webSocketResponseDto = new WebSocketResponseDto(WebSocketResponseDto.TRADING_QUOTE, tradingQuote);

        when(productContext.getProductId()).thenReturn("prodId");

        tradingQuoteHandler.handleTradingQuote(webSocketResponseDto);

        verify(rules, times(1)).parallelStream();
    }

    @Test
    public void handleTradingQuoteDifferentProductId() {
        TradingQuote tradingQuote = new TradingQuote("oldProdId", 12345D);
        WebSocketResponseDto webSocketResponseDto = new WebSocketResponseDto(WebSocketResponseDto.TRADING_QUOTE, tradingQuote);

        when(productContext.getProductId()).thenReturn("newProdId");

        tradingQuoteHandler.handleTradingQuote(webSocketResponseDto);

        verify(rules, times(0)).parallelStream();
    }

    @Test
    public void handleTradingQuoteMessageWrongType() {
        WebSocketResponseDto webSocketResponseDto = new WebSocketResponseDto("wrongType", null);

        tradingQuoteHandler.handleTradingQuote(webSocketResponseDto);

        verify(productContext, times(0)).getProductId();
    }
}