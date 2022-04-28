package nl.stokpop.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.domain.BurnerMessage;
import nl.stokpop.afterburner.domain.ParallelInfo;
import nl.stokpop.afterburner.util.Calculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Execute some tasks using the default fork join pool.
 * See what happens when it gets busy.
 */
@RestController
public class GoParallel {

    private final AfterburnerProperties props;

    public GoParallel(AfterburnerProperties props) {
        this.props = props;
    }

    @Operation(summary = "Show current information of the common fork join pool.")
    @GetMapping("/parallel-info")
    public ParallelInfo parallelInfo() {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int activeThreadCount = forkJoinPool.getActiveThreadCount();
        int parallelism = forkJoinPool.getParallelism();
        boolean asyncMode = forkJoinPool.getAsyncMode();
        int poolSize = forkJoinPool.getPoolSize();
        long stealCount = forkJoinPool.getStealCount();
        int queuedSubmissionCount = forkJoinPool.getQueuedSubmissionCount();
        long queuedTaskCount = forkJoinPool.getQueuedTaskCount();
        int runningThreadCount = forkJoinPool.getRunningThreadCount();

        ParallelInfo parallelInfo = ParallelInfo.builder()
            .name("ForkJoinPool.commonPool()")
            .activeThreadCount(activeThreadCount)
            .parallelism(parallelism)
            .asyncMode(asyncMode)
            .poolSize(poolSize)
            .queuedSubmissionCount(queuedSubmissionCount)
            .queuedTaskCount(queuedTaskCount)
            .runningThreadCount(runningThreadCount)
            .stealCount(stealCount)
            .build();

        return parallelInfo;
    }

    @Operation(summary = "Calculate the sum of prime numbers, with some additional delay, using parallel stream (common fork join pool).")
    @GetMapping("/parallel")
    public BurnerMessage goParallel(
        @RequestParam(value = "primeDelayMillis", defaultValue = "2") int primeDelayMillis,
        @RequestParam(value = "maxPrime", defaultValue = "10000") int maxPrime) {
        long startTime = System.currentTimeMillis();

        List<Long> numbers = Collections.synchronizedList(
            LongStream.range(1, maxPrime)
                .boxed()
                .collect(Collectors.toCollection(() -> new ArrayList<>(maxPrime))));

        long sum = numbers.parallelStream()
            .filter(n -> Calculator.isPrime(n, primeDelayMillis))
            .reduce(0L, Long::sum);

        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("The sum of prime numbers up to %d: %d", maxPrime, sum), props.getName(), durationMillis);
    }

    @Operation(summary = "Calculate the sum of prime numbers, with some additional delay, using a regular (serial) stream.")
    @GetMapping("/serial-stream")
    public BurnerMessage goSerialStream(
        @RequestParam(value = "primeDelayMillis", defaultValue = "5") int primeDelayMillis,
        @RequestParam(value = "maxPrime", defaultValue = "10000") long maxPrime) {
        long startTime = System.currentTimeMillis();

        long sum = LongStream.range(1, maxPrime)
            .filter(n -> Calculator.isPrime(n, primeDelayMillis))
            .reduce(0L, Long::sum);

        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(String.format("The sum of prime numbers up to %d: %d", maxPrime, sum), props.getName(), durationMillis);
    }

}
