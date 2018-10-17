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
public class BuyRulesTest {

    @InjectMocks
    private Rules buyRule = new BuyRule();

    @Mock
    private ProductContext productContext;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void isActive() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.empty());
        when(productContext.getBuyingPrice()).thenReturn(444D);

        assertTrue(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveCurrentPriceToLow() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.empty());
        when(productContext.getBuyingPrice()).thenReturn(666D);

        assertFalse(buyRule.isActive(tradingQuote));
    }

    @Test
    public void isActiveAlreadyBought() {
        WsQuote tradingQuote = new WsQuote("prodId", 555D);

        when(productRepository.findById("prodId")).thenReturn(Optional.of(new Product("prodId", "positionId", 1D)));

        assertFalse(buyRule.isActive(tradingQuote));
    }
}