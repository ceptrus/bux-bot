package com.bux.trading.bot.rules;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.repository.Product;
import com.bux.trading.bot.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpperLimitSellRulesTest {

    @InjectMocks
    private Rules buyRule = new UpperLimitSellRule();

    @Mock
    private ProductContext productContext;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void isActive() {
        WsQuote tradingQuote = new WsQuote("prodId", 666D);

        when(productRepository.findById("prodId")).thenReturn(Optional.of(new Product("prodId", "positionId", 555D)));
        when(productContext.getUpperLimitPrice()).thenReturn(600D);

        assertTrue(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveCurrentPriceToLow() {
        WsQuote tradingQuote = new WsQuote("prodId", 444D);

        when(productRepository.findById("prodId")).thenReturn(Optional.of(new Product("prodId", "positionId", 500)));
        when(productContext.getUpperLimitPrice()).thenReturn(555D);

        assertFalse(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveNotBoughtYet() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.empty());

        assertFalse(buyRule.isActive(tradingQuote));
    }
}