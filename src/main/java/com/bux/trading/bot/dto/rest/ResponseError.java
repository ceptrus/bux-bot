package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseError {
    private String message;
    private String developerMessage;
    private String errorCode;
}
