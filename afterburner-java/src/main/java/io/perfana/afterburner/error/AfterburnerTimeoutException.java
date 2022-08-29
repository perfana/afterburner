package io.perfana.afterburner.error;

import io.perfana.afterburner.AfterburnerException;

public class AfterburnerTimeoutException extends AfterburnerException {
    public AfterburnerTimeoutException(String message) {
        super(message);
    }
}
