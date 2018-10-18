package com.bux.trading.bot.config;

import com.bux.trading.bot.config.websocket.WebSocketConfigurator;
import com.bux.trading.bot.config.websocket.WebSocketEncoder;
import com.bux.trading.bot.config.websocket.WebSocketEndpoint;
import com.bux.trading.bot.service.WsMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {

    @Autowired
    private List<WsMessageHandler> wsMessageHandlers;

    @Value("${bux.authorization.header}")
    private String authorization;

    @Value("${bux.language.header}")
    private String language;

    @Value("${bux.ws.uri}")
    private String wsUri;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", authorization);
            headers.add("Accept-language", language);
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

    @Bean
    public Session session() {
        try {
            WebSocketConfigurator webSocketConfigurator = new WebSocketConfigurator(authorization, language);
            WebSocketEndpoint endpointConfig = new WebSocketEndpoint(wsMessageHandlers);

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(webSocketConfigurator)
                    .encoders(Collections.singletonList(WebSocketEncoder.class))
                    .build();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            return container.connectToServer(endpointConfig, clientEndpointConfig, new URI(wsUri));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
