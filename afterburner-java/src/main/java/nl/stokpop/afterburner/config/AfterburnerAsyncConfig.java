package nl.stokpop.afterburner.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import nl.stokpop.afterburner.AfterburnerProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

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

        final String executorName = "afterburner-executor";

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(props.getAsyncMaxPoolSize());
        executor.setCorePoolSize(props.getAsyncCorePoolSize());
        if (props.getAsyncQueueSize() != -1) {
            executor.setQueueCapacity(props.getAsyncQueueSize());
        }
        executor.setThreadNamePrefix(executorName);
        executor.initialize();

        // enable metrics for this executor
        Executor monitoredExecutor = ExecutorServiceMetrics.monitor(registry, executor.getThreadPoolExecutor(), executorName);

        // enable tracing for @Async: have one parent trace instead of all separate traces
        return new LazyTraceExecutor(this.beanFactory, monitoredExecutor);
    }

    // needed to reactivate the jvm metrics after registering executor (???)
    // tip from stackoverflow below, but that is about security
    // https://stackoverflow.com/questions/57607445/spring-actuator-jvm-metrics-not-showing-when-globalmethodsecurity-is-enabled
    @Bean
    InitializingBean forcePrometheusPostProcessor(BeanPostProcessor meterRegistryPostProcessor, PrometheusMeterRegistry registry) {
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("AfterburnerTaskScheduler-");
        return threadPoolTaskScheduler;
    }

}
