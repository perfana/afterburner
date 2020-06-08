package nl.stokpop.afterburner.controller;

import lombok.Value;

@Value
public class BurnerMessage {

    String message;
    String name;
    long durationInMillis;

}
