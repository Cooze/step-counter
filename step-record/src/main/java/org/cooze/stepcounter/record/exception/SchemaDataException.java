package org.cooze.stepcounter.record.exception;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-06
 **/
public class SchemaDataException extends Exception {
    public SchemaDataException() {
        super();
    }

    public SchemaDataException(String message) {
        super(message);
    }

    public SchemaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaDataException(Throwable cause) {
        super(cause);
    }

    protected SchemaDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
