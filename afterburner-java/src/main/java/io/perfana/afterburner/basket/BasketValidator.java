package io.perfana.afterburner.basket;

import io.perfana.afterburner.basket.validate.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasketValidator {
    private final List<Validator> validators;
    private List<String> errors;

    @Autowired
    public BasketValidator(List<Validator> validators) {
        this.validators = validators;
    }

    public boolean validate(BasketRequest request) {
        boolean isValid = true;
        errors = new ArrayList<>();
        for (Validator validator : validators) {
            boolean valid = validator.validate(request);
            if (!valid) {
                errors.addAll(validator.getErrors());
                isValid = false;
            }
        }
        return isValid;
    }

    public List<String> getErrors() {
        return errors;
    }
}