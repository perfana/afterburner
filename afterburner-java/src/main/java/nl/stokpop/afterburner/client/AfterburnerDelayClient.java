package nl.stokpop.afterburner.client;

import nl.stokpop.afterburner.domain.BurnerMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "afterburnerDelayClient", url = "${afterburner.remote.call.base_url:localhost:8080}")
public interface AfterburnerDelayClient {

    @GetMapping("/delay")
    BurnerMessage delay(@RequestParam(value = "duration", defaultValue = "100") String duration);

}
