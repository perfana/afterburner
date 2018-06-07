package nl.stokpop.afterburner.client;

public class AfterburnerClientException extends Exception {

    public AfterburnerClientException(final String message) {
        super(message);
    }

    public AfterburnerClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
