package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.client.AfterburnerClient;
import nl.stokpop.afterburner.client.AfterburnerClientException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoteBurner {

    private AfterburnerClient client;

    @RequestMapping("remote")
    public String remote(@RequestParam(value = "url", defaultValue = "/") String remoteUrl) {
        try {
            return client.remote(remoteUrl);
        } catch (AfterburnerClientException e) {
            throw new AfterburnerException("Calling remote url failed.", e);
        }
    }
}
