package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import com.bux.trading.bot.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Rules {

    @Autowired
    private ProductRepository productRepository;

    public abstract void apply(TradingQuote tradingQuote);
    public abstract boolean isActive(TradingQuote tradingQuote);

    Product product(TradingQuote tradingQuote) {
        return productRepository.findById(tradingQuote.getSecurityId())
                .orElse(null);
    }
}
