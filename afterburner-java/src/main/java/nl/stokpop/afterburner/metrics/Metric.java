package nl.stokpop.afterburner.metrics;

import lombok.Value;

@Value
public class Metric {
    String name;
    long value;
    String unit;
}
