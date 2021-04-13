package nl.stokpop.afterburner.error;

import nl.stokpop.afterburner.AfterburnerException;

public class AfterburnerCiruitBreakerException extends AfterburnerException {
    public AfterburnerCiruitBreakerException(String message) {
        super(message);
    }
}
