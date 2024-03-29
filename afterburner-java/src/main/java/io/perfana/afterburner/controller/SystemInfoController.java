package io.perfana.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.perfana.afterburner.domain.SystemInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SystemInfoController {

    @Operation(summary = "Show system info for this jvm, memory, cpu and threads.")
    @GetMapping("system-info")
    public SystemInfo systemInfo() {
        Runtime runtime = Runtime.getRuntime();

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        List<String> threadNames = threadSet.stream()
            .map(t -> t.getName() + "-" + t.getState())
            .sorted()
            .collect(Collectors.toList());

        return SystemInfo.builder()
            .availableProcessors(runtime.availableProcessors())
            .freeMemory(runtime.freeMemory())
            .maxMemory(runtime.maxMemory())
            .totalMemory(runtime.totalMemory())
            .threads(threadSet.size())
            .threadNames(threadNames)
            .build();
    }
}
