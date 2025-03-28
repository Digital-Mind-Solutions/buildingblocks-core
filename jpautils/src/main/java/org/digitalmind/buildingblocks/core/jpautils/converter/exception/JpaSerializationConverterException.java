package org.digitalmind.buildingblocks.core.jpautils.converter.exception;

public class JpaSerializationConverterException extends JpaConverterException  {

    public JpaSerializationConverterException() {
    }

    public JpaSerializationConverterException(String message) {
        super(message);
    }

    public JpaSerializationConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaSerializationConverterException(Throwable cause) {
        super(cause);
    }

    public JpaSerializationConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
