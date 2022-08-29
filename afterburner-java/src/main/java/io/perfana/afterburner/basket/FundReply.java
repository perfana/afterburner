package io.perfana.afterburner.basket;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class FundReply {
    String customer;
    long totalAmount;
    boolean hasSufficientFunds;
}
