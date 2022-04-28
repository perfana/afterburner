package nl.stokpop.afterburner.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.extern.slf4j.Slf4j;
import nl.stokpop.afterburner.AfterburnerProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class AfterburnerAsyncConfig implements AsyncConfigurer {

    private final MeterRegistry registry;
    private final BeanFactory beanFactory;
    private final AfterburnerProperties props;

    AfterburnerAsyncConfig(MeterRegistry registry, BeanFactory beanFactory, AfterburnerProperties props) {
        this.registry = registry;
        this.beanFactory = beanFactory;
        this.props = props;
    }

    @Override
    public Executor getAsyncExecutor() {
        if (props.isCustomExecutorEnabled()) {
            final String executorName = "afterburner-executor";

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setMaxPoolSize(props.getAsyncMaxPoolSize());
            executor.setCorePoolSize(props.getAsyncCorePoolSize());
            if (props.getAsyncQueueSize() != -1) {
                executor.setQueueCapacity(props.getAsyncQueueSize());
            }
            executor.setKeepAliveSeconds(props.getAsyncKeepAliveSeconds());
            // if executor is overloaded, run task in callers thread
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.setThreadNamePrefix(executorName);
            executor.initialize();
            executor.setDaemon(true);

            log.info("created async pool: " + executor);

            // enable metrics for this executor
            Executor monitoredExecutor = ExecutorServiceMetrics.monitor(registry, executor.getThreadPoolExecutor(), executorName);

            // enable tracing for @Async: have one parent trace instead of all separate traces
            return new LazyTraceExecutor(this.beanFactory, monitoredExecutor);
        }
        else {
            // "simulate" the default task executor or the executor created via spring xml config
            // thread.pool.size=10
            // thread.pool.keep.alive=30
            // <task:executor id="threadPoolTaskExecutor" pool-size="${thread.pool.size}" keep-alive="${thread.pool.keep.alive}"  />
            ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.setCorePoolSize(10);
            threadPoolTaskExecutor.setKeepAliveSeconds(30);
            threadPoolTaskExecutor.initialize();
            return threadPoolTaskExecutor;
        }
    }
}
