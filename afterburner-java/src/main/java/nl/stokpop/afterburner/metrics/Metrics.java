package nl.stokpop.afterburner.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
public class Metrics {
    @JsonProperty("instance_index")
    int instanceIndex;
    @Singular
    List<Metric> metrics;
}
