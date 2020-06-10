package nl.stokpop.afterburner.basket.validate;

import nl.stokpop.afterburner.basket.BasketRequest;
import nl.stokpop.afterburner.util.Sleeper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TotalAmountCheckValidator implements Validator {
    private List<String> errors;

    @Override
    public boolean validate(BasketRequest request) {

        errors = new ArrayList<>();

        long totalPrice = request.getPrices().stream().collect(Collectors.summarizingLong(Long::longValue)).getSum();
        String customer = request.getCustomer();
        Long expectedPrice = request.getTotalPrice();
        if (expectedPrice != totalPrice) {
            errors.add("There seems to be a problem dear '" + customer + "'." +
                    " The total should be: " + expectedPrice + " but is " + totalPrice + ".");
        }
        Sleeper.sleep(Duration.ofMillis(4));
        return errors.isEmpty();
    }

    @Override
    public Collection<String> getErrors() {
        return errors;
    }
}
