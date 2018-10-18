package com.bux.trading.bot.service;

import com.bux.trading.bot.config.ProductContext;
import com.bux.trading.bot.dto.websockets.WsResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectedHandlerTest {

    @InjectMocks
    private ConnectedHandler connectedHandler = new ConnectedHandler();

    @Mock
    private ProductContext productContext;

    @Test
    public void handleMessageNotConnected() {
        WsResponse webSocketResponseDto = new WsResponse("connect.notConnected", null);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        connectedHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(productContext, times(0)).getProductId();
    }

    @Test
    public void handleMessageNoType() {
        WsResponse webSocketResponseDto = new WsResponse(null, null);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        connectedHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(productContext, times(0)).getProductId();
    }

    @Test(expected = RuntimeException.class)
    public void handleMessageInterruptedException() {
        WsResponse webSocketResponseDto = new WsResponse(null, null);

        JsonElement jsonElement = new Gson().toJsonTree(webSocketResponseDto, WsResponse.class);

        when(productContext.getProductId()).thenThrow(new IOException(""));

        connectedHandler.handleMessage(jsonElement.getAsJsonObject(), null);

        verify(productContext, times(1)).getProductId();
    }
}