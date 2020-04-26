package org.digitalmind.buildingblocks.core.requestcontext.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class RequestContextException extends RuntimeException {

    public RequestContextException() {
    }

    public RequestContextException(String message) {
        super(message);
    }

    public RequestContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestContextException(Throwable cause) {
        super(cause);
    }

    public RequestContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
