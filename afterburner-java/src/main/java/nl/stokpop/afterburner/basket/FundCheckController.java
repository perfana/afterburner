package nl.stokpop.afterburner.basket;

import brave.Span;
import brave.Tracer;
import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class FundCheckController {

    private static final Logger log = LoggerFactory.getLogger(FundCheckController.class);

    private final Tracer tracer;

    // hmmm... makes it thread safe?
    private Random random = ThreadLocalRandom.current();

    public FundCheckController(Tracer tracer) {
        this.tracer = tracer;
    }

    @ApiOperation(value = "Check funds for customer.")
    @GetMapping(value = "/fund/check", produces = "application/json")
    public ResponseEntity<FundReply> checkFund(@RequestParam String customer, @RequestParam long amount) {
        long startTime = System.currentTimeMillis();

        boolean hasFund = false;

        Span fundCheckSpan = tracer.nextSpan().name("backend-fund-check").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(fundCheckSpan.start())) {
            // simulate some processing/db check time
            Sleeper.sleep(Duration.ofMillis(10));
            hasFund = random.nextGaussian() < 0.1;
        }
        finally {
            fundCheckSpan.tag("customer", customer)
                    .tag("amount", String.valueOf(amount))
                    .tag("fund-check-result", String.valueOf(hasFund))
                    .finish();
        }

        FundReply reply = FundReply.builder()
                .customer(customer)
                .totalAmount(amount)
                .hasSufficientFunds(hasFund)
                .build();

        long durationMillis = System.currentTimeMillis() - startTime;
        log.info("Check funds. Duration ms: " + durationMillis);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }
}
