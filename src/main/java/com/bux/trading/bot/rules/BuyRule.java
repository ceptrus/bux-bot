package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BuyRule extends Rules {

    @Override
    public void apply(TradingQuote tradingQuote) {
        Product product = product(tradingQuote);

        // You already bought it, so you can't buy again
        if (product != null) {
            return;
        }

        log.info(String.format("Opening position at %.2f", tradingQuote.getCurrentPrice()));

        ResponseOrder responseOrder = super.openPosition(tradingQuote);

        log.info(String.format("Buying price: %.2f", responseOrder.getPrice().getAmount()));
    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) == null && tradingQuote.getCurrentPrice() >= productContext.getBuyingPrice();
    }
}
