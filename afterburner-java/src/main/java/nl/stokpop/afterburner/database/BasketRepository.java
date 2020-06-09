package nl.stokpop.afterburner.database;

import nl.stokpop.afterburner.domain.Basket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Long> {

    List<Basket> findByCustomer(String customer);

    @Override
    List<Basket> findAll();

    Basket findById(long id);

}
