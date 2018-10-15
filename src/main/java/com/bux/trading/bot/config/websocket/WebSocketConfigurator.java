package com.bux.trading.bot.config.websocket;

import javax.websocket.ClientEndpointConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebSocketConfigurator extends ClientEndpointConfig.Configurator {

    private String authorization;
    private String language;

    public WebSocketConfigurator(String authorization, String language) {
        this.authorization = authorization;
        this.language = language;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Authorization", Collections.singletonList(authorization));
        headers.put("Accept-Language", Collections.singletonList(language));
    }

}
