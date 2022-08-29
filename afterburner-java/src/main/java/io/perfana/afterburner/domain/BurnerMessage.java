package io.perfana.afterburner.domain;

import lombok.Value;

@Value
public class BurnerMessage {

    String message;
    String name;
    long durationInMillis;

}
