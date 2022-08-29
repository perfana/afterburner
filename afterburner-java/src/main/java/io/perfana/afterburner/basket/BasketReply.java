package io.perfana.afterburner.basket;

import lombok.Value;

@Value
public class BasketReply {
    String message;
    Long totalAmount;
    String customer;
}
