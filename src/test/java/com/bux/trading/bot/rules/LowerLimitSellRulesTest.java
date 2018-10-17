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
public class LowerLimitSellRulesTest {

    @InjectMocks
    private Rules buyRule = new LowerLimitSellRule();

    @Mock
    private ProductContext productContext;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void isActive() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.of(new Product("prodId", "positionId", 666D)));
        when(productContext.getLowerLimitPrice()).thenReturn(600D);

        assertTrue(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveCurrentPriceToHigh() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.of(new Product("prodId", "positionId", 666D)));
        when(productContext.getLowerLimitPrice()).thenReturn(444D);

        assertFalse(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveNotBoughtYet() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.empty());

        assertFalse(buyRule.isActive(tradingQuote));
    }
}