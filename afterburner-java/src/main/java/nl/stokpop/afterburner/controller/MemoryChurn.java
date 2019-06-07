package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.util.Sleeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Creates lots of local objects per request to fill young heap space.
 */
@RestController
public class MemoryChurn {

    private static final Logger log = LoggerFactory.getLogger(MemoryChurn.class);

    private final AfterburnerProperties props;

    public MemoryChurn(final AfterburnerProperties props) {
        this.props = props;
    }

    @RequestMapping("/memory/churn")
    public BurnerMessage delay(@RequestParam(value = "objects", defaultValue = "181") int objects,
                               @RequestParam(value = "duration", defaultValue = "100") String duration) {
        long startTime = System.currentTimeMillis();

        List<BigDecimal> numbers = IntStream.range(0, objects)
                .mapToObj(BigDecimal::new)
                .collect(Collectors.toList());

        Sleeper.sleep(duration);
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("This churner object creation took [%s] ms for [%d] BigDecimals and [%s] delay.", durationMillis, numbers.size(), duration), props.getAfterburnerName(), durationMillis);
    }

}
