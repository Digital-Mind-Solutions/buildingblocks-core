package org.digitalmind.buildingblocks.core.jpautils.converter.exception;

public class JpaConverterException extends Exception {

    public JpaConverterException() {
    }

    public JpaConverterException(String message) {
        super(message);
    }

    public JpaConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaConverterException(Throwable cause) {
        super(cause);
    }

    public JpaConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}