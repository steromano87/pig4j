package io.github.steromano87.pig4j;

/**
 * Image formats managed by pig4j as output formats
 */
public enum ImageFormat {
    /**
     * Bitmap image format
     */
    BMP("bmp", "image/bmp"),

    /**
     * JPEG image format
     */
    JPEG("jpg", "image/jpeg"),

    /**
     * TIFF image format
     */
    TIFF("tif", "image/tiff"),

    /**
     * GIF image format (without animations)
     */
    GIF("gif", "image/gif"),

    /**
     * PNG image format
     */
    PNG("png", "image/png");

    private final String extension;
    private final String mimeType;

    ImageFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    /**
     * Returns the standard extension for a given image format
     *
     * @return the standard file extension of the file format
     */
    public String getExtension() {
        return this.extension;
    }

    /**
     * Returns the MIME type associated to the image format
     *
     * @return the MIME type associated to the image format
     */
    public String getMimeType() {
        return this.mimeType;
    }
}
