package com.bux.trading.bot.dto.rest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ResponseOrder {
    private String id;
    private String positionId;
    private Product product;
    private Amount investingAmount;
    private Amount price;
    private Amount profitAndLoss;
    private Integer leverage;
    private TradeType direction;
    private String type;
    private Long dateCreated;
}
