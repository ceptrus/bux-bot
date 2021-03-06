package com.bux.trading.bot.config.websocket;

import com.bux.trading.bot.dto.websockets.WsSubscribeProductRequest;
import com.google.gson.Gson;

import javax.websocket.Encoder;

public class WebSocketEncoder implements Encoder.Text<WsSubscribeProductRequest> {

    private Gson gson = new Gson();

    @Override
    public String encode(WsSubscribeProductRequest object) {
        return gson.toJson(object);
    }

    @Override
    public void init(javax.websocket.EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

}
