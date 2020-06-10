package nl.stokpop.afterburner.basket.validate;

import nl.stokpop.afterburner.basket.BasketRequest;

import java.util.Collection;

public interface Validator {
    boolean validate(BasketRequest request);
    Collection<String> getErrors();
}
