package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerException;
import nl.stokpop.afterburner.service.AfterburnerRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RestController
public class RemoteCallController {

    private final AfterburnerRemote afterburnerRemote;

    @Autowired
    public RemoteCallController(final AfterburnerRemote afterburnerRemote) {
        this.afterburnerRemote = afterburnerRemote;
    }

    @ApiOperation(value = "Call one remote service.")
    @GetMapping("remote/call")
    public String remoteCallHttpClient(
            @RequestParam(value = "path", defaultValue = "/delay") String path,
            @RequestParam(value = "type", defaultValue = "httpclient") String type) {
        return afterburnerRemote.executeCall(path, type);
    }

    @ApiOperation(value = "Call many remote services in parallel using CompletableFutures.")
    @GetMapping("remote/call-many")
    public String remoteCallHttpClientMany(
            @RequestParam(value = "path", defaultValue = "/delay") String path,
            @RequestParam(value = "type", defaultValue = "httpclient") String type,
            @RequestParam(value = "count", defaultValue = "10") int count) {

            List<CompletableFuture<String>> results = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                results.add(afterburnerRemote.executeCallAsync(path, type));
            }
            return results.stream().map(stringCompletableFuture -> {
                try {
                    return stringCompletableFuture.get(60, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new AfterburnerException("Execute async failed.", e);
                }
            }).collect(Collectors.joining(","));
    }

}
