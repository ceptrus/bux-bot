package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.TradingQuote;
import org.springframework.stereotype.Component;

@Component
public class UpperLimitSellRule extends Rules {

    @Override
    public void apply(TradingQuote tradingQuote) {
        ResponseOrder responseOrder = super.closePosition(tradingQuote);

        System.out.println(String.format("Profit: %.2f", responseOrder.getProfitAndLoss().getAmount()));
    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) != null && tradingQuote.getCurrentPrice() >= productContext.getUpperLimitPrice();
    }
}
