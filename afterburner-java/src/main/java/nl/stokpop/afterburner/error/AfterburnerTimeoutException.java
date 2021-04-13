package nl.stokpop.afterburner.error;

import nl.stokpop.afterburner.AfterburnerException;

public class AfterburnerTimeoutException extends AfterburnerException {
    public AfterburnerTimeoutException(String message) {
        super(message);
    }
}
