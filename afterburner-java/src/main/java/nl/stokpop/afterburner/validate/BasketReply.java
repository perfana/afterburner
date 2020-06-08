package nl.stokpop.afterburner.validate;

import lombok.Value;

@Value
public class BasketReply {
    String message;
    Long totalAmount;
    String customer;
}
