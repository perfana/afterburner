package nl.stokpop.afterburner.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ParallelInfo {
    String name;
    int activeThreadCount;
    int parallelism;
    boolean asyncMode;
    int poolSize;
    int queuedSubmissionCount;
    long queuedTaskCount;
    int runningThreadCount;
    long stealCount;
}
