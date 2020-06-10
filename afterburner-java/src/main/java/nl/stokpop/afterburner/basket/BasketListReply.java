package nl.stokpop.afterburner.basket;

import lombok.Singular;
import lombok.Value;
import nl.stokpop.afterburner.domain.Basket;

import java.util.List;

@Value
public class BasketListReply {
    @Singular
    List<Basket> baskets;
}
