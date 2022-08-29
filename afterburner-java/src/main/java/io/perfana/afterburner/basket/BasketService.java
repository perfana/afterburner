package io.perfana.afterburner.basket;

import io.perfana.afterburner.domain.Basket;
import io.perfana.afterburner.database.BasketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService {

    private final BasketRepository repository;

    public BasketService(BasketRepository repository) {
        this.repository = repository;
    }

    public void addBasket(Basket basket) {
        repository.save(basket);
    }

    public List<Basket> findAllBaskets() {
        return repository.findAll();
    }
}
