package com.bux.trading.bot.config;

import com.bux.trading.bot.config.websocket.WebSocketConfigurator;
import com.bux.trading.bot.config.websocket.WebSocketEncoder;
import com.bux.trading.bot.config.websocket.WebSocketEndpoint;
import com.bux.trading.bot.dto.websockets.WebSocketResponseDto;
import com.bux.trading.bot.service.TradingQuoteHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import java.net.URI;
import java.util.Collections;

@Configuration
public class AppConfig {

    @Autowired
    private ProductContext productContext;

    @Autowired
    private TradingQuoteHandler tradingQuoteHandler;

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
            Gson gson = new Gson();
            WebSocketConfigurator webSocketConfigurator = new WebSocketConfigurator(authorization, language);
            WebSocketEndpoint endpointConfig = new WebSocketEndpoint(productContext);

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(webSocketConfigurator)
                    .encoders(Collections.singletonList(WebSocketEncoder.class))
                    .build();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Session session = container.connectToServer(endpointConfig, clientEndpointConfig, new URI(wsUri));

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    tradingQuoteHandler.handleTradingQuote(gson.fromJson(message, WebSocketResponseDto.class));
                }
            });
            return session;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
