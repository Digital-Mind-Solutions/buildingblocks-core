package org.digitalmind.buildingblocks.core.phoneutils.exception;

public class PhoneUtilException extends Exception {

    public PhoneUtilException() {
    }

    public PhoneUtilException(String message) {
        super(message);
    }

    public PhoneUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneUtilException(Throwable cause) {
        super(cause);
    }

    public PhoneUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
