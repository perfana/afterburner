package nl.stokpop.afterburner;

import nl.stokpop.afterburner.domain.BigFatBastard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MemoryLeak {

    private List<BigFatBastard> bigList = new ArrayList<>();

    private final AfterburnerProperties props;

    public MemoryLeak(final AfterburnerProperties props) {
        this.props = props;
    }

    @RequestMapping("/memory/grow")
    public BurnerHello memoryGrow(
            @RequestParam(value = "objects", defaultValue = "10") int objects,
            @RequestParam(value = "items", defaultValue = "9") int items) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < objects; i++) {
            bigList.add(new BigFatBastard(items));
        }
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerHello(message, props.getAfterburnerName(), durationMillis);
    }
    
    @RequestMapping("/memory/clear")
    public BurnerHello memoryClear() {
        long startTime = System.currentTimeMillis();
        this.bigList = new ArrayList<>();
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerHello(message, props.getAfterburnerName(), durationMillis);
    }

    private String createMemoryMessage() {
        return String.format("Size of BigFatBastard list is now %d objects. " +
                "It contains %d items.", bigList.size(), sumBigFatBastardElements());
    }

    private int sumBigFatBastardElements() {
        return bigList.stream().mapToInt(BigFatBastard::size).sum();
    }

}
