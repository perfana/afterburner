package io.perfana.afterburner.basket;

import io.perfana.afterburner.basket.validate.ProducstAndPricesValidator;
import io.perfana.afterburner.basket.validate.TotalAmountCheckValidator;
import io.perfana.afterburner.basket.validate.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class BasketConfig {

    @Bean
    List<Validator> validators(TotalAmountCheckValidator totalAmountCheckValidator) {
        List<Validator> validators = new ArrayList<>();
        validators.add(new ProducstAndPricesValidator());
        // total amount check validator is a @component due to needing a remote call
        validators.add(totalAmountCheckValidator);
        return Collections.unmodifiableList(validators);
    }

}
