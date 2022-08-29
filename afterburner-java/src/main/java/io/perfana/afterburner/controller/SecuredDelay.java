package io.perfana.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.perfana.afterburner.AfterburnerProperties;
import io.perfana.afterburner.domain.BurnerMessage;
import io.perfana.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredDelay {

    private static final Logger log = LoggerFactory.getLogger(SecuredDelay.class);

    private final AfterburnerProperties props;

    public SecuredDelay(final AfterburnerProperties props) {
        this.props = props;
    }

    @Secured("ROLE_USER")
    @Operation(summary = "The secured delay call does a simple java sleep in request thread for 'duration' milliseconds.")
    @GetMapping(value = "/secured-delay", produces = "application/json" )
    public BurnerMessage delay(@RequestParam(value = "duration", defaultValue = "100") String duration) {
        long startTime = System.currentTimeMillis();
        Sleeper.sleep(duration);
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("This was a secured delay of %s", duration), props.getName(), durationMillis);
    }
}
