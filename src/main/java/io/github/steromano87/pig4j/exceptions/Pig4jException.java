package io.github.steromano87.pig4j.exceptions;

/**
 * The base exception class of Pig4j library.
 */
public class Pig4jException extends RuntimeException {
    public Pig4jException() {
    }

    public Pig4jException(String message) {
        super(message);
    }

    public Pig4jException(String message, Throwable cause) {
        super(message, cause);
    }

    public Pig4jException(Throwable cause) {
        super(cause);
    }

    public Pig4jException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
