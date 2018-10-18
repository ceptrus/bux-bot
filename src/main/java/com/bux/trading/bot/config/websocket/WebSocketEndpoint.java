package com.bux.trading.bot.config.websocket;

import com.bux.trading.bot.service.WsMessageHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Endpoint;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.util.List;

@Slf4j
public class WebSocketEndpoint extends Endpoint {

    private List<WsMessageHandler> wsMessageHandlers;

    private Gson gson = new Gson();

    public WebSocketEndpoint(List<WsMessageHandler> wsMessageHandlers) {
        this.wsMessageHandlers = wsMessageHandlers;
    }

    @Override
    public void onOpen(Session session, javax.websocket.EndpointConfig config) {

        MessageHandler.Whole<String> messageHandler = new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                JsonObject jsonElement = gson.fromJson(message, JsonObject.class);
                wsMessageHandlers.forEach(wsMessageHandler -> wsMessageHandler.handleMessage(jsonElement, session));
            }
        };
        session.addMessageHandler(messageHandler);
    }

}
