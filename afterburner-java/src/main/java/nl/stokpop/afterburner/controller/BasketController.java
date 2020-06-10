package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.basket.*;
import nl.stokpop.afterburner.domain.Basket;
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

    private static final Logger log = LoggerFactory.getLogger(Delay.class);

    private final AfterburnerProperties props;

    private final BasketValidator basketValidator;

    private final BasketService basketService;

    public BasketController(AfterburnerProperties props, BasketValidator basketValidator, BasketService basketService) {
        this.props = props;
        this.basketValidator = basketValidator;
        this.basketService = basketService;
    }

    @ApiOperation(value = "Simulates a basket purchase request with validation. What can possibly go wrong?")
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
        log.info("Basket purchase " + (validateSuccess ? "success" : "failure") + ". For " + customer + ". Duration ms: " + durationMillis);
        HttpStatus status = validateSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        BasketReply basketReply = new BasketReply(message, request.getTotalPrice(), customer);
        return new ResponseEntity<>(basketReply, status);
    }

    @ApiOperation(value = "Store a purchase in the database.")
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
        log.info("Basket store for " + customer + ". Duration ms: " + durationMillis);
        String message = "Basket stored: " + basket;
        BasketReply basketReply = new BasketReply(message, request.getTotalPrice(), customer);
        return new ResponseEntity<>(basketReply, HttpStatus.OK);
    }

    @ApiOperation(value = "Store a purchase in the database.")
    @GetMapping(value = "/basket/all", produces = "application/json")
    public ResponseEntity<BasketListReply> all() {
        long startTime = System.currentTimeMillis();
        List<Basket> allBaskets = basketService.findAllBaskets();
        BasketListReply reply = new BasketListReply(allBaskets);
        long durationMillis = System.currentTimeMillis() - startTime;
        log.info("Basket find all. Duration ms: " + durationMillis);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }
}
