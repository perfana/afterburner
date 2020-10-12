package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BurnerMessage;
import nl.stokpop.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;

@RestController
public class FlakyService {

    private static final Logger log = LoggerFactory.getLogger(FlakyService.class);

    private final AfterburnerProperties props;

    private final Random random = new Random(System.currentTimeMillis() + 39283479298L);

    public FlakyService(final AfterburnerProperties props) {
        this.props = props;
    }

    @ApiOperation(value = "The flaky call fails most of the time.")

    @GetMapping(value = "/flaky", produces = "application/json")
    public BurnerMessage flaky(
        @Parameter(name = "flakiness", description = "Percentage of flakiness. Will throw an exception this many times out of a 100 calls.")
        @RequestParam(value = "flakiness", defaultValue = "50") int flakiness,
        @Parameter(name = "maxRandomDelay", description = "If -1 a 20 millisecond sleep is done, otherwise a random sleep till maxRandomDelay in millis. Allowed: -1 to infinity (well, max Java int)")
        @RequestParam(value = "maxRandomDelay", defaultValue = "-1") int maxRandomDelay) {

        log.info("Flaky service called with flakiness percentage {} and max random delay in ms {}", flakiness, maxRandomDelay);
        long startTime = System.currentTimeMillis();

        long sleepTime = randomSleep(maxRandomDelay);

        if ((random.nextInt(100) + 1) < flakiness) {
            throw new AfterburnerException("Sorry, flaky call failed after " + sleepTime + " milliseconds .");
        }
        else {
            long durationMillis = System.currentTimeMillis() - startTime;
            return new BurnerMessage("Yes, flaky call succeeds", props.getName(), durationMillis);
        }
    }

    private long randomSleep(int maxRandomDelay) {
        long sleepTime = maxRandomDelay == -1 ? 20 : random.nextInt(maxRandomDelay) + 1;
        Sleeper.sleep(Duration.ofMillis(sleepTime));
        return sleepTime;
    }
}
