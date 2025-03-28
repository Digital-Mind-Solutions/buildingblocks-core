package org.digitalmind.buildingblocks.core.jpautils.converter.exception;

public class JpaEncryptionConverterException extends JpaConverterException  {

    public JpaEncryptionConverterException() {
    }

    public JpaEncryptionConverterException(String message) {
        super(message);
    }

    public JpaEncryptionConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaEncryptionConverterException(Throwable cause) {
        super(cause);
    }

    public JpaEncryptionConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
