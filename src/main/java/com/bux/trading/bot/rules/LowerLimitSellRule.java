package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.websockets.TradingQuote;
import org.springframework.stereotype.Component;

@Component
public class LowerLimitSellRule extends Rules {
    @Override
    public void apply(TradingQuote tradingQuote) {

    }

    @Override
    public boolean isActive(TradingQuote tradingQuote) {
        return product(tradingQuote) != null;
    }
}
