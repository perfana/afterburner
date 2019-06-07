package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Sleeper.sleep(duration);
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("This was a delay of %s", duration), props.getAfterburnerName(), durationMillis);
    }


}
