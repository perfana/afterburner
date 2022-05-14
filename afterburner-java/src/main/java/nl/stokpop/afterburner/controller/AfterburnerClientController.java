package nl.stokpop.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.stokpop.afterburner.client.AfterburnerCpuClient;
import nl.stokpop.afterburner.client.AfterburnerDefaultClient;
import nl.stokpop.afterburner.client.AfterburnerDelayClient;
import nl.stokpop.afterburner.domain.BurnerMessage;
import nl.stokpop.afterburner.matrix.InvalidMatrixException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class AfterburnerClientController {

    private AfterburnerDelayClient afterburnerDelayClient;

    private AfterburnerCpuClient afterburnerCpuClient;

    private AfterburnerDefaultClient afterburnerDefaultClient;

    @Operation(summary = "Spend some time on CPU doing some magic matrix calculations.")
    @GetMapping("/client/cpu/magic-identity-check")
    public BurnerMessage magicIdentityCheck(
            @RequestParam(value = "matrixSize", defaultValue = "10") int matrixSize) throws InvalidMatrixException {
        log.info("Call afterburner cpu client magicIdentityCheck with matrixSize {}", matrixSize);
        return afterburnerCpuClient.magicIdentityCheck(matrixSize);
    }

    @Operation(summary = "The delay call does a simple java sleep in request thread for 'duration' milliseconds.")
    @GetMapping(value = "/client/delay", produces = "application/json" )
    public BurnerMessage delay(@RequestParam(value = "duration", defaultValue = "100") String duration) {
        log.info("Call afterburner delay client delay with duration {}", duration);
        return afterburnerDelayClient.delay(duration);
    }

    @GetMapping(value = "/client/flaky", produces = "application/json")
    public BurnerMessage flaky(
            @Parameter(name = "flakiness", description = "Percentage of flakiness. Will throw an exception this many times out of a 100 calls.")
            @RequestParam(value = "flakiness", defaultValue = "50") int flakiness,
            @Parameter(name = "maxRandomDelay", description = "If -1 a 20 millisecond sleep is done, otherwise a random sleep till maxRandomDelay in millis. Allowed: -1 to infinity (well, max Java int)")
            @RequestParam(value = "maxRandomDelay", defaultValue = "-1") int maxRandomDelay) {
        log.info("Call afterburner default client flaky with flakiness {} maxRandomDelay {}", flakiness, maxRandomDelay);
        return afterburnerDefaultClient.flaky(flakiness, maxRandomDelay);
    }

}
