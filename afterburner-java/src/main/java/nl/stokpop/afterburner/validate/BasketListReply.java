package nl.stokpop.afterburner.validate;

import lombok.Singular;
import lombok.Value;
import nl.stokpop.afterburner.domain.Basket;

import java.util.List;

@Value
public class BasketListReply {
    @Singular
    List<Basket> baskets;
}
