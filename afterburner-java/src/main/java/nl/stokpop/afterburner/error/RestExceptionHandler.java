package nl.stokpop.afterburner.error;

import nl.stokpop.afterburner.AfterburnerException;
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

    private final static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleGeneralException(final Exception ex, final WebRequest request) {
        
        String devMessage = String.format("%s: %s", ex.getClass().getSimpleName(), getExceptionMessageChain(ex));
        String userMessage = String.format("The afterburner failed for the following reason: %s", ex.getMessage());

        HttpStatus returnCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage errorMessage = new ErrorMessage(devMessage, userMessage, returnCode.value());

        if (ex instanceof AfterburnerException) {
            log.error(devMessage);
        }
        else {
            log.error(devMessage, ex);
        }
        
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), returnCode, request);
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
