package nl.stokpop.afterburner.util;

import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.controller.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public final class Sleeper {

    private static final Logger log = LoggerFactory.getLogger(Delay.class);

    public static void sleep(String duration) {
        Duration sleepInMillis = parseDurationIntoMillis(duration);
        log.info("About to sleep for [{}] millis (duration = [{}]).", sleepInMillis, duration);
        sleep(sleepInMillis);
    }

    public static void sleep(Duration sleepDuration) {
        try {
            Thread.sleep(sleepDuration.toMillis());
        } catch (InterruptedException e) {
            log.warn("Sleep received interrupt: {}", e.getMessage());
        }
    }

    private static Duration parseDurationIntoMillis(String duration) {
        Duration sleepDuration;
        if (duration.startsWith("P")) {
            sleepDuration = determineISO8601DurationInMillis(duration);
        }
        else {
            try {
                sleepDuration = Duration.ofMillis(Long.parseLong(duration));
            } catch (NumberFormatException e) {
                throw new AfterburnerException(String.format("Not a valid duration [%s]", duration), e);
            }
        }
        return sleepDuration;
    }

    private static Duration determineISO8601DurationInMillis(String duration) {
        Duration parsedDuration;
        try {
            parsedDuration = Duration.parse(duration);
        } catch (DateTimeParseException e) {
            throw new AfterburnerException(String.format("Not a valid duration [%s]", duration), e);
        }
        return parsedDuration;
    }

}
