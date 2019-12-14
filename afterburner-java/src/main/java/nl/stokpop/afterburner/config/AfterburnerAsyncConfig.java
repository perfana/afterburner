package nl.stokpop.afterburner.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AfterburnerAsyncConfig implements AsyncConfigurer {

    private final MeterRegistry registry;

    AfterburnerAsyncConfig(final MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Executor getAsyncExecutor() {

        final String executorName = "afterburner-executor";

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix(executorName);
        executor.initialize();

        // enable metrics for this executor
        return ExecutorServiceMetrics.monitor(registry, executor.getThreadPoolExecutor(), executorName);
    }

}
