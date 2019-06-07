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
        long sleepInMillis = parseDurationIntoMillis(duration);
        log.info("About to sleep for [{}] millis (duration = [{}]).", sleepInMillis, duration);
        sleep(sleepInMillis);
    }

    private static void sleep(long sleepInMillis) {
        try {
            Thread.sleep(sleepInMillis);
        } catch (InterruptedException e) {
            log.warn("Sleep received interrupt: {}", e.getMessage());
        }
    }

    public static long parseDurationIntoMillis(String duration) {
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
        return sleepInMillis;
    }

    public static long determineISO8601DurationInMillis(String duration) {
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
