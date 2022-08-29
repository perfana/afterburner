package io.perfana.afterburner.basket;

import io.perfana.afterburner.domain.Basket;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
public class BasketListReply {
    @Singular
    List<Basket> baskets;
}
