package nl.stokpop.afterburner.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.client.RemoteCallException;
import nl.stokpop.afterburner.client.RemoteCallHttpClient;
import nl.stokpop.afterburner.client.RemoteCallOkHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.Future;

@Service
public class AfterburnerRemote {

    private final Counter counterTotalCalls;

    private final RemoteCallHttpClient httpClient;
    private final RemoteCallOkHttp okHttp;

    @Autowired
    public AfterburnerRemote(RemoteCallHttpClient httpClient, RemoteCallOkHttp okHttp, MeterRegistry registry, AfterburnerProperties props) {
        this.httpClient = httpClient;
        this.okHttp = okHttp;
        this.counterTotalCalls = registry.counter("remote.calls", "name", props.getName());
    }

    public String executeCall(@RequestParam(value = "path", defaultValue = "/") String path, @RequestParam(value = "type", defaultValue = "httpclient") String type) throws IOException {

        counterTotalCalls.increment();

        switch (type) {
            case "httpclient":
                return httpClient.call(path);
            case "okhttp":
                return okHttp.call(path);
            default:
                throw new RemoteCallException(String.format("Unknown remote call type [%s]", type));
        }
    }

    @Async
    public Future<String> executeCallAsync(@RequestParam(value = "path", defaultValue = "/") String path, @RequestParam(value = "type", defaultValue = "httpclient") String type) throws IOException {
        return new AsyncResult<>(executeCall(path, type));
    }
}
