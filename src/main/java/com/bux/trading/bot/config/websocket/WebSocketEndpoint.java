package com.bux.trading.bot.config.websocket;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsResponse;
import com.bux.trading.bot.dto.websockets.WsSubscribeProductRequest;
import com.bux.trading.bot.service.TradingQuoteHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Endpoint;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.concurrent.ExecutionException;

@Slf4j
public class WebSocketEndpoint extends Endpoint {
    private static final String TRADING_QUOTE = "trading.quote";
    private static final String CONNECT_CONNECTED = "connect.connected";

    private ProductContext productContext;
    private TradingQuoteHandler tradingQuoteHandler;

    private Gson gson = new Gson();

    public WebSocketEndpoint(ProductContext productContext, TradingQuoteHandler tradingQuoteHandler) {
        this.productContext = productContext;
        this.tradingQuoteHandler = tradingQuoteHandler;
    }

    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig config) {

        MessageHandler.Whole<String> connectedHandler = new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try {
                    JsonElement jsonElement = gson.fromJson(message, JsonObject.class).get("t");
                    if (jsonElement == null) {
                        return;
                    }

                    String t = jsonElement.getAsString();

                    if (CONNECT_CONNECTED.equals(t)) {
                        WsSubscribeProductRequest subscribeRequestDto = new WsSubscribeProductRequest();
                        subscribeRequestDto.addSubscribeTo(productContext.getProductId());
                        session.getAsyncRemote().sendObject(subscribeRequestDto).get();

                    } else if (TRADING_QUOTE.equals(t)) {
                        WsResponse wsResponse = gson.fromJson(message, WsResponse.class);
                        tradingQuoteHandler.handleTradingQuote(wsResponse);
                    } else {
                        log.error("Body type error for message: " + message);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Not able to connect to WebSocket", e);
                    throw new RuntimeException(e);
                }
            }
        };
        session.addMessageHandler(connectedHandler);
    }

}
