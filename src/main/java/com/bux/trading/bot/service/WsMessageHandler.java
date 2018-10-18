package com.bux.trading.bot.service;

import com.google.gson.JsonObject;

import javax.websocket.Session;

public interface WsMessageHandler {
    void handleMessage(JsonObject jsonObject, Session session);
}
