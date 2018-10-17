package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import com.bux.trading.bot.repository.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpperLimitSellRule extends Rules {

    @Override
    public void apply(TradingQuote tradingQuote) {
        Product product = product(tradingQuote);
        if (product == null) {
            String msg = String.format("Product not found %s", tradingQuote.getSecurityId());
            log.error(msg);
            throw new RuntimeException(msg);
        }

        log.info(String.format("Closing position at %.2f", tradingQuote.getCurrentPrice()));

        ResponseOrder responseOrder = super.closePosition(product);

        log.info(String.format("Profit: %.2f", responseOrder.getProfitAndLoss().getAmount()));
    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) != null && tradingQuote.getCurrentPrice() >= productContext.getUpperLimitPrice();
    }
}
