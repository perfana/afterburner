package nl.stokpop.afterburner;

public class AfterburnerException extends RuntimeException {
    public AfterburnerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
