package io.perfana.afterburner.error;

import io.perfana.afterburner.AfterburnerException;

public class AfterburnerCiruitBreakerException extends AfterburnerException {
    public AfterburnerCiruitBreakerException(String message) {
        super(message);
    }
}
