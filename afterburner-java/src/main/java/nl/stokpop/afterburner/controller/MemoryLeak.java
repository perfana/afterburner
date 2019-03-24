package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BigFatBastard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryLeak {

    private static final Logger log = LoggerFactory.getLogger(MemoryLeak.class);

    private List<BigFatBastard> bigList = new ArrayList<>();

    private final AfterburnerProperties props;
    private final String name;

    public MemoryLeak(final AfterburnerProperties props) {
        this.props = props;
        this.name = props.getAfterburnerName() == null
                ? String.format("Anonymous Burner [%s]", this.toString())
                : props.getAfterburnerName();
    }

    @RequestMapping("/memory/grow")
    public BurnerMessage memoryGrow(
            @RequestParam(value = "objects", defaultValue = "10") int objects,
            @RequestParam(value = "items", defaultValue = "9") int items) {

        long startTime = System.currentTimeMillis();

        log.info("Add [{}] objects with [{}] items to Big list with size [{}].", objects, items, bigList.size());

        for (int i = 0; i < objects; i++) {
            bigList.add(new BigFatBastard(items));
        }
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(message, name, durationMillis);
    }
    
    @RequestMapping("/memory/clear")
    public BurnerMessage memoryClear() {
        log.info("Clear Big list with size [{}].", bigList.size());
        long startTime = System.currentTimeMillis();
        this.bigList = new ArrayList<>();
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(message, props.getAfterburnerName(), durationMillis);
    }

    private String createMemoryMessage() {
        return String.format("Size of BigFatBastard list is now %d objects. " +
                "It contains %d items.", bigList.size(), sumBigFatBastardElements());
    }

    private int sumBigFatBastardElements() {
        return bigList.stream().mapToInt(BigFatBastard::size).sum();
    }

}
