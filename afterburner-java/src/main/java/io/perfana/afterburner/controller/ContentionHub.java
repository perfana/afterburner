package io.perfana.afterburner.controller;

import io.perfana.afterburner.AfterburnerProperties;
import io.perfana.afterburner.domain.BurnerMessage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.perfana.afterburner.util.Sleeper.sleep;

@RestController
public class ContentionHub {

    public static final Object lock = new Object();
    private final AfterburnerProperties props;

    public ContentionHub(final AfterburnerProperties props) {
        this.props = props;
    }

    @Operation(summary = "One lock to hold all threads.")
    @GetMapping(value = "/one-lock", produces = "application/json" )
    public BurnerMessage oneLock(@RequestParam(value = "duration", defaultValue = "100") String duration) {
        long startTime = System.currentTimeMillis();
        synchronized (lock) {
            sleep(duration);
        }
        long lockDuration = System.currentTimeMillis() - startTime;
        return new BurnerMessage("Survived lock wait", props.getName(), lockDuration);
    }

}
