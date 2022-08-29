package io.perfana.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.perfana.afterburner.AfterburnerException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

@RestController
public class AsynExceptionController {

    private static final Logger log = LoggerFactory.getLogger(AsynExceptionController.class);
    private final AsyncTaskExecutor executor;


    @Autowired
    public AsynExceptionController(@Qualifier("threadPoolTaskScheduler") Executor executor) {
        this.executor = (AsyncTaskExecutor) executor;
    }

    @Operation(summary = "Call CompletableFuture with timeout and exceptions.")
    @GetMapping("async/exception")
    public String asyncException(
            @RequestParam(value = "sleepMillis", defaultValue = "2000") long sleepMillis,
            @RequestParam(value = "doException", defaultValue = "true") boolean doException) {

        Future<String> future = executor.submit(() -> {
            try {
                return doYourThing(sleepMillis, doException);
            } catch (Exception e) {
                log.error("Exception from within the executor.", e);
                throw e;
            }
        });

        String exceptionMessage;
        try {
            String message = future.get(1, TimeUnit.SECONDS);
            log.info("Got {}", message);
            return message;
        } catch (InterruptedException e) {
            log.info("Got InterruptedException", e);
            exceptionMessage = e.toString();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.info("Got ExecutionException", e);
            exceptionMessage = e.toString();
        } catch (TimeoutException e) {
            log.info("Got TimeoutException", e);
            exceptionMessage = e.toString();
        }
        return "Got exception! " + exceptionMessage;
    }

    @NotNull
    private String doYourThing(long sleepMillis, boolean doException) throws InterruptedException {
        Thread.sleep(sleepMillis);
        if (doException) {
            throw new AfterburnerException("Something went wrong in async");
        }
        return "Hello Async World";
    }
}
