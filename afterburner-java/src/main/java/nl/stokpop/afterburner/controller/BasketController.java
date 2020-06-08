package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.validate.BasketReply;
import nl.stokpop.afterburner.validate.BasketRequest;
import nl.stokpop.afterburner.validate.BasketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BasketController {

    private static final Logger log = LoggerFactory.getLogger(Delay.class);

    private final AfterburnerProperties props;

    private final BasketValidator basketValidator;

    public BasketController(AfterburnerProperties props, BasketValidator basketValidator) {
        this.props = props;
        this.basketValidator = basketValidator;
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
}
