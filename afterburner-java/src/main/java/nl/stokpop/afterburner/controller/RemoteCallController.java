package nl.stokpop.afterburner.controller;

import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.client.RemoteCallException;
import nl.stokpop.afterburner.client.RemoteCallHttpClient;
import nl.stokpop.afterburner.client.RemoteCallOkHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemoteCallController {

    private final RemoteCallHttpClient httpClient;
    private final RemoteCallOkHttp okHttp;

    @Autowired
    public RemoteCallController(final RemoteCallHttpClient httpClient, final RemoteCallOkHttp okHttp) {
        this.httpClient = httpClient;
        this.okHttp = okHttp;
    }

    @RequestMapping("remote/call")
    public String remoteCallHttpClient(
            @RequestParam(value = "path", defaultValue = "/") String path,
            @RequestParam(value = "type", defaultValue = "httpclient") String type) {
        try {
            switch (type) {
                case "httpclient": return httpClient.call(path);
                case "okhttp": return okHttp.call(path);
                default: throw new RemoteCallException(String.format("Unknown remote call type [%s]", type));
            }
        } catch (RemoteCallException e) {
            throw new AfterburnerException("Call to remote url via HttpClient failed.", e);
        }
    }

}
