package nl.stokpop.afterburner.validate;

import java.util.Collection;

public interface Validator {
    boolean validate(BasketRequest request);
    Collection<String> getErrors();
}
