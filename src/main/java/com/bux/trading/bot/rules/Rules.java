package com.bux.trading.bot.rules;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.rest.*;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.repository.Product;
import com.bux.trading.bot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class Rules {

    @Autowired
    protected ProductContext productContext;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Value("${bux.api.uri}")
    private String apiUrl;

    @Value("${orders.place.amout}")
    private double amountToBuy;

    public abstract void apply(WsQuote wsQuote);
    public abstract boolean isActive(WsQuote wsQuote);

    protected Product product(WsQuote wsQuote) {
        return productRepository.findById(wsQuote.getSecurityId())
                .orElse(null);
    }

    protected ResponseOrder openPosition(WsQuote wsQuote) {
        String productId = wsQuote.getSecurityId();

        RequestOrder requestOrder = RequestOrder.builder()
                .direction(TradeType.BUY)
                .investingAmount(new Amount("BUX", 2, amountToBuy))
                .leverage(2)
                .productId(productId)
                .source(new Source(SourceType.OTHER))
                .build();

        ResponseEntity<ResponseOrder> orderResponseEntity = restTemplate.postForEntity(getApiTradeUrl(), requestOrder, ResponseOrder.class);
        String positionId = orderResponseEntity.getBody().getPositionId();

        productRepository.save(new Product(productId, positionId, wsQuote.getCurrentPrice()));

        return orderResponseEntity.getBody();
    }

    protected ResponseOrder closePosition(Product product) {
        String url = getApiClosePositionUrl(product);
        ResponseEntity<ResponseOrder> exchange = restTemplate.exchange(url, HttpMethod.DELETE, null, ResponseOrder.class);
        productRepository.delete(product);

        return exchange.getBody();
    }

    protected String getApiUrl() {
        return apiUrl.endsWith("/") ? apiUrl : apiUrl + "/";
    }

    protected String getApiTradeUrl() {
        return getApiUrl() + "core/21/users/me/trades";
    }

    protected String getApiClosePositionUrl(Product product) {
        return getApiUrl() + String.format("core/21/users/me/portfolio/positions/%s", product.getPositionId());
    }
}
