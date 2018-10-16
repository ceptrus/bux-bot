package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import org.springframework.stereotype.Component;

@Component
public class BuyRule extends Rules {

    @Override
    public void apply(TradingQuote tradingQuote) {
        Product product = product(tradingQuote);

        if (product != null) {
            return;
        }

        ResponseOrder responseOrder = super.openPosition(tradingQuote);

        System.out.println(String.format("Buying price: %.2f", responseOrder.getPrice().getAmount()));
    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) == null && tradingQuote.getCurrentPrice() >= productContext.getBuyingPrice();
    }
}
