package com.github.steromano87.pig4j.exceptions;

public class ConfigReadingException extends Pig4jException {
    public ConfigReadingException() {
    }

    public ConfigReadingException(String message) {
        super(message);
    }

    public ConfigReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigReadingException(Throwable cause) {
        super(cause);
    }

    public ConfigReadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
