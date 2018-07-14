package nl.stokpop.afterburner.client;

import nl.stokpop.afterburner.AfterburnerException;

public class RemoteCallException extends AfterburnerException {
    
    public RemoteCallException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RemoteCallException(final String message) {
        super(message);
    }
}
