package com.bux.trading.bot.rules;

import com.bux.trading.bot.dto.rest.ResponseOrder;
import com.bux.trading.bot.dto.websockets.WsQuote;
import com.bux.trading.bot.repository.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpperLimitSellRule extends Rules {

    @Override
    public void apply(WsQuote wsQuote) {
        Product product = product(wsQuote);
        if (product == null) {
            String msg = String.format("Product not found %s", wsQuote.getSecurityId());
            log.error(msg);
            throw new RuntimeException(msg);
        }

        log.info(String.format("Closing position at %.2f", wsQuote.getCurrentPrice()));

        ResponseOrder responseOrder = super.closePosition(product);

        log.info(String.format("Profit: %.2f", responseOrder.getProfitAndLoss().getAmount()));
    }

    @Override
    public boolean isActive(WsQuote wsQuote) {
        Product product = product(wsQuote);
        return product != null && wsQuote.getCurrentPrice() >= productContext.getUpperLimitPrice()
                && wsQuote.getCurrentPrice() > product.getPrice();
    }
}
