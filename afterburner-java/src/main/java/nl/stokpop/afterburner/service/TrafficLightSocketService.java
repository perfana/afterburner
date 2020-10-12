package nl.stokpop.afterburner.service;

import nl.stokpop.afterburner.util.Sleeper;
import nl.stokpop.afterburner.util.TrafficLightSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class TrafficLightSocketService {

    private static final Logger log = LoggerFactory.getLogger(TrafficLightSocketService.class);

    public static final int TRAFFIC_LIGHT_LISTEN_PORT = 5599;

    /**
     * Every 5 seconds the traffic light service is active on port 5599.
     * It sends a reply when an empty line is read from the incoming traffic.
     * And next, the traffic light service is unreachable.
     *
     * Example:
     * nc -v -w 5 localhost 5599
     * Connection to localhost port 5599 [tcp/esinstall] succeeded!
     * [press enter]
     * The traffic light is green!
     *
     * @throws IOException when something fails in connecting to network
     */
    @Scheduled(fixedDelay = 5000)
    public void enableTrafficLightForSomeTime() throws IOException {
        log.debug("TrafficLightService green on " + TRAFFIC_LIGHT_LISTEN_PORT);

        final AtomicInteger threadCounter = new AtomicInteger(0);
        final ThreadFactory threadFactory =
            (r) -> new Thread(r, "traffic-light-thread-" + threadCounter.incrementAndGet());

        ExecutorService executor = Executors.newFixedThreadPool(6, threadFactory);

        TrafficLightSocketListener socketListener = new TrafficLightSocketListener(TRAFFIC_LIGHT_LISTEN_PORT, executor);
        executor.execute(socketListener);

        Sleeper.sleep(Duration.of(5, SECONDS));

        socketListener.stopServing();
        executor.shutdownNow();
        log.debug("TrafficLightService red");
    }

}
