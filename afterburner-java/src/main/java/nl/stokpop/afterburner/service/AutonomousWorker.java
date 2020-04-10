package nl.stokpop.afterburner.service;

import lombok.extern.slf4j.Slf4j;
import nl.stokpop.afterburner.util.Sleeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class AutonomousWorker {

    @Value("${afterburner.autonomous.worker.stability:true}")
    private volatile boolean stability;

    @Scheduled(fixedDelay = 10_000, initialDelay = 5_000)
    public void doSomeFixedDelayWork() {
        String threadName = Thread.currentThread().getName();
        log.info("\\\\ {} start some fixed delay work", threadName);
        Sleeper.sleep(Duration.ofSeconds(2));
        if (!stability) {
            maybeThrowExceptionOrHang(threadName, "Some work fixed delay work failed miserably in %s.");
        }
        log.info("// {} end some fixed delay work", threadName);
    }

    @Scheduled(fixedRate = 10_000, initialDelay = 5_000)
    public void doSomeFixedRateWork() {
        String threadName = Thread.currentThread().getName();
        log.info("++ {} initiated some fixed rate work", threadName);
        Sleeper.sleep(Duration.ofSeconds(2));
        if (!stability) {
            maybeThrowExceptionOrHang(threadName, "Some work fixed rate work in error in %s.");
        }
        log.info("-- {} finished some fixed rate work", threadName);
    }

    private void maybeThrowExceptionOrHang(String threadName, String s) {
        if (Math.random() < 0.05) {
            throw new RuntimeException(String.format(s, threadName));
        }
        if (Math.random() < 0.001) {
            hangThread();
        }
    }

    private void hangThread() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
