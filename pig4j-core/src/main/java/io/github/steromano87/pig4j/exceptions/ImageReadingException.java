package io.github.steromano87.pig4j.exceptions;

/**
 * Exception thrown when an image file cannot be read.
 */
public class ImageReadingException extends Pig4jException {
    public ImageReadingException() {
    }

    public ImageReadingException(String message) {
        super(message);
    }

    public ImageReadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageReadingException(Throwable cause) {
        super(cause);
    }

    public ImageReadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
