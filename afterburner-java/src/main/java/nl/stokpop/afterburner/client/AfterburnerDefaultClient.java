package nl.stokpop.afterburner.client;

import io.swagger.v3.oas.annotations.Parameter;
import nl.stokpop.afterburner.domain.BurnerMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "default", url = "${afterburner.remote.call.base_url:localhost:8080}")
public interface AfterburnerDefaultClient {

    @GetMapping(value = "/flaky", produces = "application/json")
    BurnerMessage flaky(
            @Parameter(name = "flakiness", description = "Percentage of flakiness. Will throw an exception this many times out of a 100 calls.")
            @RequestParam(value = "flakiness", defaultValue = "50") int flakiness,
            @Parameter(name = "maxRandomDelay", description = "If -1 a 20 millisecond sleep is done, otherwise a random sleep till maxRandomDelay in millis. Allowed: -1 to infinity (well, max Java int)")
            @RequestParam(value = "maxRandomDelay", defaultValue = "-1") int maxRandomDelay);
}
