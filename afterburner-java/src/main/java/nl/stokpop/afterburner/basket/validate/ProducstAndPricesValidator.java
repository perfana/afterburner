package nl.stokpop.afterburner.basket.validate;

import nl.stokpop.afterburner.basket.BasketRequest;
import nl.stokpop.afterburner.util.Sleeper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProducstAndPricesValidator implements Validator {
    private List<String> errors;

    @Override
    public boolean validate(BasketRequest request) {

        errors = new ArrayList<>();

        String customer = request.getCustomer();
        int productCount = request.getProducts().size();
        int priceCount = request.getPrices().size();
        if (priceCount != productCount) {
            errors.add("There seems to be a problem dear '" + customer + "'." +
                    " The total number of products: " + productCount + " but there are " + priceCount + " prices." +
                    " The products: " + request.getProducts() +
                    " The prices: " + request.getPrices());
        }
        Sleeper.sleep(Duration.ofMillis(4));
        return errors.isEmpty();
    }

    @Override
    public Collection<String> getErrors() {
        return errors;
    }
}
