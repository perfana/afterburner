package nl.stokpop.afterburner.controller;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ParallelInfo {
    private String name;
    private int activeThreadCount;
    private int parallelism;
    private boolean asyncMode;
    private int poolSize;
    private int queuedSubmissionCount;
    private long queuedTaskCount;
    private int runningThreadCount;
    private long stealCount;
}
