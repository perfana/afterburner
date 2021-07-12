package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BurnerMessage;
import nl.stokpop.afterburner.domain.MusicMachine;
import nl.stokpop.afterburner.domain.MusicMachineMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MemoryLeak {

    private static final Logger log = LoggerFactory.getLogger(MemoryLeak.class);

    private final AfterburnerProperties props;
    private final String name;

    public MemoryLeak(final AfterburnerProperties props) {
        this.props = props;
        this.name = props.getName() == null
                ? String.format("Anonymous Burner [%s]", this)
                : props.getName();
    }

    @ApiOperation(value = "Simulate a memory leak.")
    @GetMapping("/memory/grow")
    public BurnerMessage memoryGrow(
            @RequestParam(value = "objects", defaultValue = "10") int objects,
            @RequestParam(value = "items", defaultValue = "9") int items,
            @RequestParam(value = "length", defaultValue = "100") int length) {

        long startTime = System.currentTimeMillis();

        log.info("Add [{}] objects with [{}] item of length [{}] to current set of [{}] objects.",
            objects, items, length, MusicMachine.getMusicMachineMemories().size());

        for (int i = 0; i < objects; i++) {
            MusicMachine.getMusicMachineMemories().add(new MusicMachineMemory(items, length));
        }
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(message, name, durationMillis);
    }

    @ApiOperation(value = "Clear the memory leak.")
    @GetMapping("/memory/clear")
    public BurnerMessage memoryClear() {
        log.info("Clear object list with size [{}].", MusicMachine.getMusicMachineMemories().size());
        long startTime = System.currentTimeMillis();
        MusicMachine.setMusicMachineMemories(new ArrayList<>());
        String message = createMemoryMessage();
        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(message, props.getName(), durationMillis);
    }

    private String createMemoryMessage() {
        return String.format("There are now %d objects, " +
                "containing a total of %d items.", MusicMachine.getMusicMachineMemories().size(), sumMusicMemoryElements());
    }

    private int sumMusicMemoryElements() {
        return MusicMachine.getMusicMachineMemories().stream().mapToInt(MusicMachineMemory::size).sum();
    }

}
