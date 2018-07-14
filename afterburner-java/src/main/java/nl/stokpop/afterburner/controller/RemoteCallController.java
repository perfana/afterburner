package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.client.RemoteCall;
import nl.stokpop.afterburner.client.RemoteCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoteCallController {

    private final RemoteCall client;

    /**
     * Use the @Qualifier to switch to another RemoteCall implementation:
     * remote-call-http-client, remote-call-okhttp-client
     */
    @Autowired
    public RemoteCallController(@Qualifier("remote-call-http-client") final RemoteCall client) {
        this.client = client;
    }

    @RequestMapping("remote/call")
    public String remoteCallHttpClient(@RequestParam(value = "path", defaultValue = "/") String path) {
        try {
            return client.call(path);
        } catch (RemoteCallException e) {
            throw new AfterburnerException("Call to remote url via HttpClient failed.", e);
        }
    }

}
