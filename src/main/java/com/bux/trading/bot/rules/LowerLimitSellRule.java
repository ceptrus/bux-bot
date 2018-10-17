package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.repository.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LowerLimitSellRule extends Rules {

    @Override
    public void apply(WsQuote tradingQuote) {
        Product product = product(tradingQuote);
        if (product == null) {
            String msg = String.format("Product not found %s", tradingQuote.getSecurityId());
            log.error(msg);
            throw new RuntimeException(msg);
        }

        log.info(String.format("Closing position at %.2f", tradingQuote.getCurrentPrice()));

        ResponseOrder responseOrder = super.closePosition(product);

        log.info(String.format("Loss: %.2f", responseOrder.getProfitAndLoss().getAmount()));
    }

    @Override
    public boolean isActive(WsQuote tradingQuote) {
        Product product = product(tradingQuote);
        return product != null && tradingQuote.getCurrentPrice() <= productContext.getLowerLimitPrice()
                && tradingQuote.getCurrentPrice() < product.getPrice();
    }
}
