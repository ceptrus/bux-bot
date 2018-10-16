package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import org.springframework.stereotype.Component;

@Component
public class LowerLimitSellRule extends Rules {
    @Override
    public void apply(TradingQuote tradingQuote) {
        ResponseOrder responseOrder = super.closePosition(tradingQuote);

        System.out.println(String.format("Loss: %.2f", responseOrder.getProfitAndLoss().getAmount()));
    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) != null && tradingQuote.getCurrentPrice() <= productContext.getLowerLimitPrice();
    }
}
