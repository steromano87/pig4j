package com.github.steromano87.pig4j.exceptions;

public class ConfigWritingException extends Pig4jException {
    public ConfigWritingException() {
    }

    public ConfigWritingException(String message) {
        super(message);
    }

    public ConfigWritingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigWritingException(Throwable cause) {
        super(cause);
    }

    public ConfigWritingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
