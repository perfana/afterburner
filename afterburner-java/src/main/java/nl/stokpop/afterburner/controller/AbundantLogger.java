package nl.stokpop.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BurnerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@RestController
public class AbundantLogger {

    private static final Logger log = LoggerFactory.getLogger(AbundantLogger.class);

    private final AfterburnerProperties props;

    public AbundantLogger(final AfterburnerProperties props) {
        this.props = props;
    }

    @Operation(summary = "This will log abundantly!")
    @PostMapping(value = "/log-some", produces = "application/json" )
    public BurnerMessage logSomeBody(@RequestBody String body, @RequestParam(value = "logLines", defaultValue = "10") int logLines) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < logLines; i++) {
            log.info("This is the body: " + body);
        }
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("Logging of %d lines took some time.", logLines), props.getName(), durationMillis);
    }

    @Operation(summary = "This will log abundantly!")
    @GetMapping(value = "/log-some", produces = "application/json" )
    public BurnerMessage logSome(@RequestParam(value = "logLines", defaultValue = "10") int logLines,
                                 @RequestParam(value = "logSize", defaultValue = "1000") int logSize) {
        long startTime = System.currentTimeMillis();

        StringBuilder logMessageBuilder = new StringBuilder(logSize);

        // filter only printable ascii characters
        IntStream.range(1, logSize)
            .map(i -> 33 + (i % 93))
            .forEach(i -> logMessageBuilder.append((char)i));

        String logMessage = logMessageBuilder.toString();

        for (int i = 0; i < logLines; i++) {
            log.info("This is the message: " + logMessage);
        }

        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("Logging of %d lines took some time.", logLines), props.getName(), durationMillis);
    }
}
