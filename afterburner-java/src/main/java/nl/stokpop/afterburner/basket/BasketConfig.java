package nl.stokpop.afterburner.basket;

import nl.stokpop.afterburner.basket.validate.ProducstAndPricesValidator;
import nl.stokpop.afterburner.basket.validate.TotalAmountCheckValidator;
import nl.stokpop.afterburner.basket.validate.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class BasketConfig {

    @Bean
    List<Validator> validators() {
        List<Validator> validators = new ArrayList<>();
        validators.add(new ProducstAndPricesValidator());
        validators.add(new TotalAmountCheckValidator());
        return Collections.unmodifiableList(validators);
    }

}
