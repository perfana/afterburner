package nl.stokpop.afterburner.basket;

import lombok.*;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class BasketRequest {
     @Singular
     List<String> products;
     @Singular
     List<Long> prices;
     Long totalPrice;
     String customer;
}
