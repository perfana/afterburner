package io.perfana.afterburner.basket.validate;

import io.perfana.afterburner.basket.BasketRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProducstAndPricesValidator implements Validator {
    private List<String> errors;

    @Override
    public boolean validate(BasketRequest request) {

        errors = new ArrayList<>();

        String customer = request.getCustomer();
        long productCount = request.getProducts().parallelStream().count();
        long priceCount = request.getPrices().parallelStream().count();
        if (priceCount != productCount) {
            errors.add("There seems to be a problem dear '" + customer + "'." +
                    " The total number of products: " + productCount + " but there are " + priceCount + " prices." +
                    " The products: " + request.getProducts() +
                    " The prices: " + request.getPrices());
        }
        //Sleeper.sleep(Duration.ofMillis(4));
        return errors.isEmpty();
    }

    @Override
    public Collection<String> getErrors() {
        return errors;
    }
}
