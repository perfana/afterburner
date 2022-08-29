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
public class MindMyBusiness {

    private static final Logger log = LoggerFactory.getLogger(MindMyBusiness.class);

    private final AfterburnerProperties props;

    public MindMyBusiness(final AfterburnerProperties props) {
        this.props = props;
    }

    @Operation(summary = "Mind my business for 'duration' milliseconds.")
    @GetMapping(value = "/mind-my-business", produces = "application/json" )
    public BurnerMessage mindMyBusiness(@RequestParam(value = "duration", defaultValue = "5") String duration) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("mindMyBusiness start");
            Sleeper.sleep(duration);
            long durationMillis = System.currentTimeMillis() - startTime;
            return new BurnerMessage(String.format("Mind my business should take %s milliseconds.", duration), props.getName(), durationMillis);
        } finally {
            log.info("mindMyBusiness end: {} ms", System.currentTimeMillis() - startTime);
        }
    }

    @Secured("ROLE_USER")
    @Operation(summary = "Mind my business for 'duration' milliseconds.")
    @GetMapping(value = "/mind-my-business-s", produces = "application/json" )
    public BurnerMessage mindMyBusinesss(@RequestParam(value = "duration", defaultValue = "5") String duration) {
        return mindMyBusiness(duration);
    }
}
