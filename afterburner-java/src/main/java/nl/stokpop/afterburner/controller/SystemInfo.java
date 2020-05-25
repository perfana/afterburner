package nl.stokpop.afterburner.controller;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SystemInfo {
    int availableProcessors;
    long maxMemory;
    long freeMemory;
    long totalMemory;
    int threads;
    List<String> threadNames;
}
