package nl.stokpop.afterburner.error;

import nl.stokpop.afterburner.AfterburnerException;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpHeaders retryHeaders;
    private static final ResponseEntity<Object> systemBusyResponse;

    static {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "120");
        retryHeaders = HttpHeaders.readOnlyHttpHeaders(headers);

        systemBusyResponse = ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .headers(retryHeaders)
            .body("Sorry, system is busy, please try again in a little while.");

    }

    private final static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException exception, final WebRequest request) {
        log.warn("ClientAbortException for [{}] with message {}", request.getDescription(true), exception.getMessage());
    }

    @ExceptionHandler(AfterburnerCiruitBreakerException.class)
    public ResponseEntity<Object> handleCircuitBreakerException(AfterburnerCiruitBreakerException exception, final WebRequest request) {
        log.error("CircuitBreakerException for [{}] with message: {}", request.getDescription(true), exception.getMessage());
        return systemBusyResponse;
    }

    @ExceptionHandler(AfterburnerTimeoutException.class)
    public ResponseEntity<Object> handleTimeoutException(AfterburnerTimeoutException exception, final WebRequest request) {
        log.error("TimeoutException for [{}] with message: {}", request.getDescription(true), exception.getMessage());
       return systemBusyResponse;
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleGeneralException(final Exception ex, final WebRequest request) {
        
        String devMessageLong = String.format("%s: %s", ex.getClass().getSimpleName(), getExceptionMessageChain(ex));
        String devMessageShort = String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
        String userMessage = String.format("The afterburner failed for the following reason: %s", ex.getMessage());

        HttpStatus returnCode = HttpStatus.INTERNAL_SERVER_ERROR;

        final String devMessageToUse;
        if (ex instanceof AfterburnerException) {
            devMessageToUse = devMessageShort;
            log.error(devMessageToUse, ex);
        }
        else if (ex instanceof ClientAbortException) {
            devMessageToUse = devMessageShort;
            log.warn(devMessageToUse);
        }
        // check for nested exception is java.sql.SQLTransientConnectionException: employee-db-pool - Connection is not available, request timed out after 1000ms.
        else if (hasException(ex, java.sql.SQLTransientConnectionException.class)) {
            devMessageToUse = devMessageShort;
            log.error(devMessageToUse);
        }
        else {
            devMessageToUse = devMessageLong;
            log.error(devMessageToUse, ex);
        }

        ErrorMessage errorMessage = new ErrorMessage(devMessageToUse, userMessage, returnCode.value());
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), returnCode, request);
    }

    private boolean hasException(Throwable throwable, Class<?> exceptionClass) {
        while (throwable != null) {
            if (exceptionClass.isAssignableFrom(throwable.getClass())) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }

    private static List<String> getExceptionMessageChain(Throwable throwable) {
        List<String> result = new ArrayList<>();
        while (throwable != null) {
            result.add(throwable.toString());
            throwable = throwable.getCause();
        }
        return result;
    }

}
