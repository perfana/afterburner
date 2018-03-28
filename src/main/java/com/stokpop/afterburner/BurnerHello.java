package com.stokpop.afterburner;

public class BurnerHello {

    private String message;
    private String name;
    private long durationInMillis;

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
