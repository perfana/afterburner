package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.domain.SystemInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class SystemInfoController {

    @ApiOperation(value = "Show system info for this jvm, memory, cpu and threads.")
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
