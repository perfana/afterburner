package nl.stokpop.afterburner.client;

import nl.stokpop.afterburner.domain.BurnerMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "afterburnerCpuClient", url = "${afterburner.remote.call.base_url:localhost:8080}")
public interface AfterburnerCpuClient {

    @GetMapping("/cpu/magic-identity-check")
    BurnerMessage magicIdentityCheck(@RequestParam(value = "matrixSize", defaultValue = "10") int matrixSize);
}
