package nl.stokpop.afterburner.stub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SimpleController {

    @GetMapping(value = "/delay", produces = MediaType.APPLICATION_JSON_VALUE)
    public String delay() {
        int delay = 100;
        log.info("Received /delay call for {} millis", delay);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return String.format("{\"message\":\"%s\",\"name\":\"%s\",\"durationInMillis\":%d}", "hello world!", "AfterburnerStub", delay);
    }

}
