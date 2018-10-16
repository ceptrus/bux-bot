package com.bux.trading.bot.rules;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.rest.*;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import com.bux.trading.bot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class Rules {

    @Autowired
    ProductContext productContext;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Value("${bux.api.uri}")
    private String apiUrl;

    @Value("${orders.place.amout}")
    private double amount;

    public abstract void apply(TradingQuote tradingQuote);
    public abstract boolean isActive(TradingQuote tradingQuote);

    Product product(TradingQuote tradingQuote) {
        return productRepository.findById(tradingQuote.getSecurityId())
                .orElse(null);
    }

    ResponseOrder openPosition(TradingQuote tradingQuote) {
        String productId = tradingQuote.getSecurityId();

        RequestOrder requestOrder = RequestOrder.builder()
                .direction(TradeType.BUY)
                .investingAmount(new Amount("BUX", 2, amount))
                .leverage(2)
                .productId(productId)
                .source(new Source(SourceType.OTHER))
                .build();

        ResponseEntity<ResponseOrder> orderResponseEntity = restTemplate.postForEntity(getApiTradeUrl(), requestOrder, ResponseOrder.class);
        String positionId = orderResponseEntity.getBody().getPositionId();

        productRepository.save(new Product(productId, positionId, tradingQuote.getCurrentPrice()));

        return orderResponseEntity.getBody();
    }

    ResponseOrder closePosition(TradingQuote tradingQuote) {
        Product product = product(tradingQuote);

        ResponseEntity<ResponseOrder> exchange = restTemplate.exchange(getApiClosePositionUrl(product), HttpMethod.DELETE, null, ResponseOrder.class);
        productRepository.delete(product);

        return exchange.getBody();
    }

    String getApiUrl() {
        return apiUrl.endsWith("/") ? apiUrl : apiUrl + "/";
    }

    String getApiTradeUrl() {
        return getApiUrl() + "core/21/users/me/trades";
    }

    String getApiClosePositionUrl(Product product) {
        return getApiUrl() + String.format("core/21/users/me/portfolio/positions/%s", product.getPositionId());
    }
}
