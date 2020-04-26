package org.digitalmind.buildingblocks.core.configutils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ConfigUtilException extends RuntimeException {

    public ConfigUtilException() {
    }

    public ConfigUtilException(String message) {
        super(message);
    }

    public ConfigUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigUtilException(Throwable cause) {
        super(cause);
    }

    public ConfigUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
