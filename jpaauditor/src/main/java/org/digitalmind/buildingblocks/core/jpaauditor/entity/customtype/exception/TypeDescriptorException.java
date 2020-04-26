package org.digitalmind.buildingblocks.core.jpaauditor.entity.customtype.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class TypeDescriptorException extends RuntimeException {

    public TypeDescriptorException() {
    }

    public TypeDescriptorException(String message) {
        super(message);
    }

    public TypeDescriptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeDescriptorException(Throwable cause) {
        super(cause);
    }

    public TypeDescriptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
