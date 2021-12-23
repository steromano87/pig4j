package com.github.steromano87.pig4j;

import com.github.steromano87.pig4j.exceptions.ImageGenerationException;
import com.github.steromano87.pig4j.exceptions.ImageWritingException;
import com.github.steromano87.pig4j.layers.Layer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

/**
 * Entry point class to generate a composite image.
 * <p>
 * This class accepts as input one or more {@link Layer} instances and generates a composite images by applying
 * all the layers in a FIFO order.
 *
 * @see Layer
 */
public class ImageGenerator {
    private final ArrayList<Layer> layers = new ArrayList<>();

    private BufferedImage processedImage;
    private final int canvasWidth;
    private final int canvasHeight;

    private final Color backgroundColor;

    private final boolean hasAlphaChannel;

    /**
     * Creates an empty image generator with white background and no alpha channel support
     *
     * @param width  the width of the image canvas, expressed in pixels
     * @param height the height of the image canvas, expressed in pixels
     */
    public ImageGenerator(int width, int height) {
        this(width, height, Color.WHITE);
    }

    /**
     * Creates an empty image generator, applying the give background color. Alpha channel support is disabled.
     *
     * @param width           the width of the image canvas, expressed in pixels
     * @param height          the height of the image canvas, expressed in pixels
     * @param backgroundColor the background color to use as lower-level color
     */
    public ImageGenerator(int width, int height, Color backgroundColor) {
        this(width, height, backgroundColor, false);
    }

    /**
     * Creates an empty image generator with alpha channel support. The background will be set as transparent.
     *
     * @param width           the width of the image canvas, expressed in pixels
     * @param height          the height of the image canvas, expressed in pixels
     * @param backgroundColor the background color to use as lower-level color
     * @param hasAlphaChannel whether the composed generator supports the alpha channel or not
     */
    public ImageGenerator(int width, int height, Color backgroundColor, boolean hasAlphaChannel) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.backgroundColor = backgroundColor;
        this.hasAlphaChannel = hasAlphaChannel;
        int imageType = this.hasAlphaChannel ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        this.processedImage = new BufferedImage(this.canvasWidth, this.canvasHeight, imageType);

        if (!Objects.isNull(backgroundColor)) {
            this.fillWithBackgroundColor(this.backgroundColor);
        }
    }

    /**
     * Adds a layer on top of the existing layers stack.
     * <p>
     * This method uses the builder pattern.
     *
     * @param layer the layer to be added
     * @return the image generator instance with the added layer
     */
    public ImageGenerator addLayer(Layer layer) {
        this.layers.add(layer);
        return this;
    }

    public int getCanvasWidth() {
        return this.canvasWidth;
    }

    public int getCanvasHeight() {
        return this.canvasHeight;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public boolean hasAlphaChannel() {
        return this.hasAlphaChannel;
    }

    /**
     * Combines the existing layers ang internally generates the final output image.
     * <p>
     * Since the generation process can be long (depending on the image size and layers number and type),
     * the image generation can be called lazily only when the final output is required.
     * <p>
     * The image generation process is non-destructive, i.e. if after the first generation the user wants
     * to add another layer and re-generate the image, existing layers will e preserved.
     *
     * @return the image generator instance, using builder pattern
     * @throws ImageGenerationException if there are errors during image generation
     */
    public ImageGenerator build() throws ImageGenerationException {
        if (this.layers.isEmpty()) {
            throw new ImageGenerationException("No layer has been added");
        }

        for (Layer layer : this.layers) {
            this.processedImage = layer.apply(this.processedImage);
        }
        return this;
    }

    /**
     * Returns the processed image as a Java buffered image (for internal use
     *
     * @return the processed image as a buffered image
     * @throws ImageGenerationException when the final image has not been processed before or if no layers have been added
     */
    public BufferedImage toImage() throws ImageGenerationException {
        return this.safelyGetProcessedImage();
    }

    /**
     * Returns the processed image as a byte array for low-level operations
     *
     * @param format the underlying image format
     * @return the processed image as a byte array
     * @throws ImageWritingException when the byte array is not writable or
     *                               if there are some errors during image processing
     */
    public byte[] toByteArray(ImageFormat format) throws ImageWritingException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(this.safelyGetProcessedImage(), format.getExtension(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException exc) {
            throw new ImageWritingException("Cannot write to output byte array", exc);
        }
    }

    /**
     * Writes the processed image to a file
     *
     * @param file   the output file
     * @param format the file format
     * @return whether the file has been written or not
     * @throws ImageWritingException if there are errors when writing the file or
     *                               if there are some errors during image processing
     */
    public boolean toFile(File file, ImageFormat format) throws ImageWritingException {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            return ImageIO.write(this.safelyGetProcessedImage(), format.getExtension(), outputStream);
        } catch (IOException exc) {
            throw new ImageWritingException("Cannot write to output file", exc);
        }
    }

    /**
     * Returns the Base64 representation of the processed image
     *
     * @param format the underlying image format
     * @return the Base64 representation of the processed image
     * @throws ImageWritingException if there are some errors during the processed image generation
     */
    public String toBase64(ImageFormat format) throws ImageWritingException {
        byte[] byteArray = this.toByteArray(format);
        return Base64.getEncoder().encodeToString(byteArray);
    }

    /**
     * Returns the data URL representation of the processed image
     * <p>
     * Data URLs are composed of the MIME type of the image and the Base64 representation of the image
     *
     * @param format the underlying image format
     * @return the data URL representation of the processed image
     * @throws ImageWritingException if there are some errors during the processed image generation
     * @link https://en.wikipedia.org/wiki/Data_URI_scheme
     */
    public String toDataUrl(ImageFormat format) throws ImageWritingException {
        String base64image = this.toBase64(format);
        String mimeType = format.getMimeType();
        return String.format("data:%s;base64,%s", mimeType, base64image);
    }

    private BufferedImage safelyGetProcessedImage() throws ImageGenerationException {
        if (Objects.isNull(this.processedImage)) {
            throw new ImageGenerationException(
                    "No image was processed. " +
                            "Ensure that at least one layer is set " +
                            "and that the build() method has been executed at least once " +
                            "before accessing the processed image"
            );
        }

        return this.processedImage;
    }

    private void fillWithBackgroundColor(Color color) {
        Graphics2D graphics2D = this.processedImage.createGraphics();
        graphics2D.setBackground(color);
        graphics2D.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
        graphics2D.dispose();
    }
}
