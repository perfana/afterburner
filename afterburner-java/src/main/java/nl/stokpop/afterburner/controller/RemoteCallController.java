package nl.stokpop.afterburner.controller;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.service.AfterburnerRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
public class RemoteCallController {

    private static final Logger log = LoggerFactory.getLogger(RemoteCallController.class);

    private final AfterburnerRemote afterburnerRemote;
    private final RetryRegistry registryHttpClient;
    private final RetryRegistry registryUrlConnection;

    @Autowired
    public RemoteCallController(final AfterburnerRemote afterburnerRemote) {
        this.afterburnerRemote = afterburnerRemote;

        RetryConfig config = RetryConfig.custom()
            .maxAttempts(10)
            .waitDuration(Duration.ofMillis(1000))
            .retryExceptions(SocketTimeoutException.class)
            .retryOnResult(result -> { System.out.println("Result (" + result.getClass().getSimpleName() + ") in predicate:" + result); return containsError(result); })
            .build();

        this.registryHttpClient = RetryRegistry.of(config);
        this.registryHttpClient.getEventPublisher().onEvent(event -> log.warn("Retry for " + event.getEventType()));

        RetryConfig configUrlConnection = RetryConfig.custom()
            .maxAttempts(10)
            .waitDuration(Duration.ofMillis(1000))
            .retryExceptions(SocketTimeoutException.class, ConnectException.class)
            .retryOnResult(result -> { System.out.println("Result (" + result.getClass().getSimpleName() + ") in predicate:" + result); return containsError(result); })
            .build();

        this.registryUrlConnection = RetryRegistry.of(configUrlConnection);
        this.registryHttpClient.getEventPublisher().onEvent(event -> log.warn("Retry for " + event.getEventType()));

    }

    private boolean containsError(Object result) {
        String resultString;
        if (result instanceof byte[]) {
            resultString = new String((byte[])result);
        }
        else if (result instanceof String) {
            resultString = (String) result;
        }
        else {
            resultString = "unknown type";
        }
        return resultString.contains("500");
    }

    @ApiOperation(value = "Call one remote service.")
    @GetMapping("remote/call")
    public String remoteCallHttpClient(
            @RequestParam(value = "path", defaultValue = "/delay") String path,
            @RequestParam(value = "type", defaultValue = "httpclient") String type) {
        try {
            return afterburnerRemote.executeCall(path, type);
        } catch (IOException e) {
            throw new AfterburnerException("Execute call failed for " + path, e);
        }
    }

    @ApiOperation(value = "Call many remote services in parallel using CompletableFutures.")
    @GetMapping("remote/call-many")
    public String remoteCallHttpClientMany(
            @RequestParam(value = "path", defaultValue = "/delay?duration=33") String path,
            @RequestParam(value = "type", defaultValue = "httpclient") String type,
            @RequestParam(value = "count", defaultValue = "3") int count) {

            List<Future<String>> results = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                try {
                    results.add(afterburnerRemote.executeCallAsync(path, type));
                } catch (IOException e) {
                    log.error("Execute call async failed for " + path + ", expect one less result", e);
                }
            }
            return results.stream().map(futureString -> {
                try {
                    return futureString.get(60, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new AfterburnerException("Execute async failed.", e);
                }
            }).collect(Collectors.joining(","));
    }

    @ApiOperation(value = "Call remote service with retries.")
    @GetMapping("remote/call-retry")
    public String remoteCallHttpClientWithRetries(
        @RequestParam(value = "path", defaultValue = "/delay") String path,
        @RequestParam(value = "type", defaultValue = "httpclient") String type) {

        Retry retryConfig = registryHttpClient.retry("afterburner-retry");

        try {
            return retryConfig.executeCallable(() -> afterburnerRemote.executeCall(path, type));
        } catch (Exception e) {
            throw new AfterburnerException("Executing callable failed.", e);
        }
    }

    @ApiOperation(value = "Call traffic light with retries.")
    @GetMapping("remote/call-traffic-light")
    public String remoteCallTrafficLightWithRetries() {

        Retry retryConfig = registryUrlConnection.retry("traffic-light-retry");

        try {
            Callable<String> remoteCall = () -> {
                URLConnection urlConnection = new URL("http://localhost:5599").openConnection();
                urlConnection.setConnectTimeout(1000);
                urlConnection.setReadTimeout(1000);
                try (Scanner scanner = new Scanner(urlConnection.getInputStream())) {
                    return scanner.useDelimiter("\\A").next();
                }
            };
            return retryConfig.executeCallable(remoteCall);
        } catch (Exception e) {
            throw new AfterburnerException("Executing callable failed.", e);
        }
    }
}
