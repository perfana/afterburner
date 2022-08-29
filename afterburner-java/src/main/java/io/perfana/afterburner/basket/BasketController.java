package io.perfana.afterburner.basket;

import io.perfana.afterburner.domain.Basket;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BasketController {

    private static final Logger log = LoggerFactory.getLogger(BasketController.class);

    private final BasketValidator basketValidator;

    private final BasketService basketService;

    public BasketController(BasketValidator basketValidator, BasketService basketService) {
        this.basketValidator = basketValidator;
        this.basketService = basketService;
    }

    @Operation(summary = "Simulates a basket purchase request with validation. What can possibly go wrong?")
    @PostMapping(value = "/basket/purchase", produces = "application/json", consumes = "application/json")
    public ResponseEntity<BasketReply> purchase(@RequestBody BasketRequest request) {
        long startTime = System.currentTimeMillis();
        String customer = request.getCustomer();
        String message;
        boolean validateSuccess = basketValidator.validate(request);
        if (!validateSuccess) {
            List<String> errors = basketValidator.getErrors();
            message = "Sorry, there are errors! " + errors;
        }
        else {
            message = "All is fine " + customer + ", happy purchase! " + request.getProducts();
        }
        long durationMillis = System.currentTimeMillis() - startTime;
        log.info("Basket purchase {} For {}. Duration ms: {}", (validateSuccess ? "success" : "failure"), customer, durationMillis);
        HttpStatus status = validateSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        BasketReply basketReply = new BasketReply(message, request.getTotalPrice(), customer);
        return new ResponseEntity<>(basketReply, status);
    }

    @Operation(description = "Store a purchase in the database.")
    @PostMapping(value = "/basket/store", produces = "application/json", consumes = "application/json")
    public ResponseEntity<BasketReply> store(@RequestBody BasketRequest request) {
        long startTime = System.currentTimeMillis();
        String customer = request.getCustomer();
        Basket basket = Basket.builder()
                .products(request.getProducts().toString())
                .prices(request.getPrices().toString())
                .customer(customer)
                .totalPrice(request.getTotalPrice())
                .build();
        basketService.addBasket(basket);
        long durationMillis = System.currentTimeMillis() - startTime;
        log.info("Basket store for {}. Duration ms: {}", customer, durationMillis);
        String message = "Basket stored: " + basket;
        BasketReply basketReply = new BasketReply(message, request.getTotalPrice(), customer);
        return new ResponseEntity<>(basketReply, HttpStatus.OK);
    }

    @Operation(description = "Store a purchase in the database.")
    @GetMapping(value = "/basket/all", produces = "application/json")
    public ResponseEntity<BasketListReply> all() {
        long startTime = System.currentTimeMillis();
        List<Basket> allBaskets = basketService.findAllBaskets();
        BasketListReply reply = new BasketListReply(allBaskets);
        long durationMillis = System.currentTimeMillis() - startTime;
        log.info("Basket find all. Duration ms: {}", durationMillis);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }

}
