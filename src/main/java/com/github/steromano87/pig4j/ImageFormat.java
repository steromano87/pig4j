package com.github.steromano87.pig4j;

public enum ImageFormat {
    BMP("bmp", "image/bmp"),
    JPEG("jpg", "image/jpeg"),
    TIFF("tif", "image/tiff"),
    GIF("gif", "image/gif"),
    PNG("png", "image/png");

    private final String extension;
    private final String mimeType;

    ImageFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getMimeType() {
        return this.mimeType;
    }
}
