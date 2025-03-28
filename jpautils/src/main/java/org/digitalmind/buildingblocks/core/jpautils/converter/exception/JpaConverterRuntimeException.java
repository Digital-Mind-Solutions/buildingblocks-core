package org.digitalmind.buildingblocks.core.jpautils.converter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class JpaConverterRuntimeException extends RuntimeException {

    public JpaConverterRuntimeException() {
    }

    public JpaConverterRuntimeException(String message) {
        super(message);
    }

    public JpaConverterRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaConverterRuntimeException(Throwable cause) {
        super(cause);
    }

    public JpaConverterRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
