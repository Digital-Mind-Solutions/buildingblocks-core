package org.digitalmind.buildingblocks.core.healthutils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class HealthApiException extends RuntimeException {

    public HealthApiException() {
    }

    public HealthApiException(String message) {
        super(message);
    }

    public HealthApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public HealthApiException(Throwable cause) {
        super(cause);
    }

    public HealthApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
