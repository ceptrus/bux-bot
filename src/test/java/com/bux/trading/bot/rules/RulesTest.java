package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.*;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import com.bux.trading.bot.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RulesTest {

    @InjectMocks
    private Rules rules = new BuyRule();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void getApiUrlTest() {
        ReflectionTestUtils.setField(rules, "apiUrl", "http://myurl.com");
        assertEquals("http://myurl.com/", rules.getApiUrl());

        ReflectionTestUtils.setField(rules, "apiUrl", "http://myurl.com");
        assertEquals("http://myurl.com/core/21/users/me/trades", rules.getApiTradeUrl());

        Product product = new Product("prdId", "positionId", 123D);
        ReflectionTestUtils.setField(rules, "apiUrl", "http://myurl.com/");
        assertEquals("http://myurl.com/core/21/users/me/portfolio/positions/positionId", rules.getApiClosePositionUrl(product));
    }

    @Test
    public void openPositionTest() {
        TradingQuote tradingQuote = new TradingQuote("prodId", 123D);

        ResponseOrder responseOrder = new ResponseOrder("id", "positionId", null, null, null, null, 2, TradeType.SELL, "type", 123L);


        RequestOrder requestOrder = RequestOrder.builder()
                .direction(TradeType.BUY)
                .investingAmount(new Amount("BUX", 2, 999D))
                .leverage(2)
                .productId(tradingQuote.getSecurityId())
                .source(new Source(SourceType.OTHER))
                .build();

        ReflectionTestUtils.setField(rules, "apiUrl", "http://myurl.com");
        ReflectionTestUtils.setField(rules, "amount", 999D);
        when(restTemplate.postForEntity(rules.getApiTradeUrl(), requestOrder, ResponseOrder.class)).thenReturn(new ResponseEntity<>(responseOrder, HttpStatus.OK));

        ResponseOrder returnResponseOrder = rules.openPosition(tradingQuote);

        assertEquals(responseOrder, returnResponseOrder);
    }

    @Test
    public void closePositionTest() {
        Product product = new Product("prodId", "positionId", 2D);

        ResponseOrder responseOrder = new ResponseOrder("id", "positionId", null, null, null, null, 2, TradeType.SELL, "type", 123L);
        ResponseEntity<ResponseOrder> responseEntity = new ResponseEntity<>(responseOrder, HttpStatus.OK);

        ReflectionTestUtils.setField(rules, "apiUrl", "http://myurl.com");
        when(restTemplate.exchange(rules.getApiClosePositionUrl(product), HttpMethod.DELETE, null, ResponseOrder.class)).thenReturn(responseEntity);

        ResponseOrder returnedResponseOrder = rules.closePosition(product);

        assertEquals(responseOrder, returnedResponseOrder);
        verify(productRepository, times(1)).delete(product);
    }

}