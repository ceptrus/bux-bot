package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsSubscribeProductRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

@Slf4j
@Service
public class ConnectedHandler implements WsMessageHandler {
    private static final String CONNECT_CONNECTED = "connect.connected";

    @Autowired
    private ProductContext productContext;

    public void handleMessage(JsonObject jsonObject, Session session) {
        JsonElement jsonElement = jsonObject.get("t");
        if (jsonElement == null) {
            return;
        }

        try {
            String t = jsonElement.getAsString();

            if (CONNECT_CONNECTED.equals(t)) {
                WsSubscribeProductRequest subscribeRequestDto = new WsSubscribeProductRequest();
                subscribeRequestDto.addSubscribeTo(productContext.getProductId());
                session.getBasicRemote().sendObject(subscribeRequestDto);
            }
        } catch (EncodeException | IOException e) {
            log.error("Not able to connect to WebSocket", e);
            throw new RuntimeException(e);
        }
    }

}
