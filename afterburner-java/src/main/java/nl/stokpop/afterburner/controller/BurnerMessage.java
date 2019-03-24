package nl.stokpop.afterburner.controller;

public class BurnerMessage {

    private final String message;
    private final String name;
    private final long durationInMillis;

    public BurnerMessage(final String message, final String name, final long durationInMillis) {
        this.message = message;
        this.name = name;
        this.durationInMillis = durationInMillis;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public long getDurationInMillis() {
        return durationInMillis;
    }
}
