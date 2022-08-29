package io.perfana.afterburner.client;

import io.perfana.afterburner.AfterburnerException;

public class RemoteCallException extends AfterburnerException {
    
    public RemoteCallException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RemoteCallException(final String message) {
        super(message);
    }
}
