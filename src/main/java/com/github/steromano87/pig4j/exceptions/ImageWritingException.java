package com.github.steromano87.pig4j.exceptions;

public class ImageWritingException extends Pig4jException {
    public ImageWritingException() {
    }

    public ImageWritingException(String message) {
        super(message);
    }

    public ImageWritingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageWritingException(Throwable cause) {
        super(cause);
    }

    public ImageWritingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
