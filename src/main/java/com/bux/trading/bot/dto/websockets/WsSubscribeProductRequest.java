package com.bux.trading.bot.dto.websockets;

import java.util.ArrayList;
import java.util.List;

public class WsSubscribeProductRequest {
    private final static String PREFIX = "trading.product.";

    private List<String> subscribeTo = new ArrayList<>();
    private List<String> unsubscribeFrom = new ArrayList<>();

    public void addSubscribeTo(String subscribeTo) {
        this.subscribeTo.add(PREFIX + subscribeTo);
    }

    public List<String> getSubscribeTo() {
        return subscribeTo;
    }

    public void addUnsubscribeFrom(String unsubscribeFrom) {
        subscribeTo.remove(PREFIX + unsubscribeFrom);
        this.unsubscribeFrom.add(PREFIX + unsubscribeFrom);
    }

    public List<String> getUnsubscribeFrom() {
        return unsubscribeFrom;
    }

}
