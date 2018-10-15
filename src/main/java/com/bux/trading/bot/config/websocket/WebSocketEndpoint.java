package com.bux.trading.bot.config.websocket;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.SubscribeRequestDto;

import javax.websocket.Endpoint;
import javax.websocket.Session;
import java.util.concurrent.ExecutionException;

public class WebSocketEndpoint extends Endpoint {

    private ProductContext productContext;

    public WebSocketEndpoint(ProductContext productContext) {
        this.productContext = productContext;
    }

    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig config) {
        try {
            SubscribeRequestDto subscribeRequestDto = new SubscribeRequestDto();
            subscribeRequestDto.addSubscribeTo(productContext.getProductId());
            session.getAsyncRemote().sendObject(subscribeRequestDto).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
