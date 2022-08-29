package io.perfana.afterburner.basket.validate;

import io.perfana.afterburner.basket.BasketRequest;

import java.util.Collection;

public interface Validator {
    boolean validate(BasketRequest request);
    Collection<String> getErrors();
}
