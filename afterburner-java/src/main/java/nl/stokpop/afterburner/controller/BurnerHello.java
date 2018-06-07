package nl.stokpop.afterburner.controller;

public class BurnerHello {

    private final String message;
    private final String name;
    private final long durationInMillis;

    public BurnerHello(final String message, final String name, final long durationInMillis) {
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
