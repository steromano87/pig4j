package com.github.steromano87.pig4j.exceptions;

public class ImageGenerationException extends Pig4jException {
    public ImageGenerationException() {
    }

    public ImageGenerationException(String message) {
        super(message);
    }

    public ImageGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageGenerationException(Throwable cause) {
        super(cause);
    }

    public ImageGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
