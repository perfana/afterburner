package io.perfana.afterburner.error;

public class OutOfResourcesException extends RuntimeException {
    public OutOfResourcesException(String message) {
        super(message);
    }
}
