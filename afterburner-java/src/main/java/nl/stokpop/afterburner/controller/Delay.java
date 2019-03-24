package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.AfterburnerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.format.DateTimeParseException;

@RestController
public class Delay {

    private static final Logger log = LoggerFactory.getLogger(Delay.class);

    private final AfterburnerProperties props;

    public Delay(final AfterburnerProperties props) {
        this.props = props;
    }

    @RequestMapping("/delay")
    public BurnerMessage delay(@RequestParam(value = "duration", defaultValue = "100") String duration) {
        long startTime = System.currentTimeMillis();

        long sleepInMillis;
        if (duration.startsWith("P")) {
            sleepInMillis = determineISO8601DurationInMillis(duration);
        }
        else {
            try {
                sleepInMillis = Long.parseLong(duration);
            } catch (NumberFormatException e) {
                throw new AfterburnerException(String.format("Not a valid duration [%s]", duration), e);
            }
        }
        log.info("About to sleep for [{}] millis (duration = [{}]).", sleepInMillis, duration);
        try {
            Thread.sleep(sleepInMillis);
        } catch (InterruptedException e) {
            log.warn("Sleep received interrupt: {}", e.getMessage());
        }
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("This was a delay of %s", duration), props.getAfterburnerName(), durationMillis);
    }

    private long determineISO8601DurationInMillis(String duration) {
        Duration parsedDuration;
        try {
            parsedDuration = Duration.parse(duration);
        } catch (DateTimeParseException e) {
            throw new AfterburnerException(String.format("Not a valid duration [%s]", duration), e);
        }
        long seconds = parsedDuration.getSeconds();
        int nano = parsedDuration.getNano();
        return seconds * 1_000 + nano / 1_000_000;
    }


}
