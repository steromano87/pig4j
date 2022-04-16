package io.github.steromano87.pig4j.layers.base;

import io.github.steromano87.pig4j.exceptions.ImageReadingException;
import io.github.steromano87.pig4j.Layer;
import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.RotationOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Layers that adds an image on top of the existing stack.
 */
@EqualsAndHashCode
@ToString
public class ImageLayer implements Layer {
    private BufferedImage sourceImage;

    private File imageFile;
    private URL imageUrl;
    private String imageBase64;

    private ScalingOptions scalingOptions = new ScalingOptions();
    private RotationOptions rotationOptions = new RotationOptions();
    private PositionOptions positionOptions = new PositionOptions();
    private BlendingOptions blendingOptions = new BlendingOptions();

    /**
     * Sets the source file of the image.
     *
     * @param imageFile the image file
     * @return the layer
     */
    public ImageLayer setImage(File imageFile) {
        this.imageFile = imageFile;
        try {
            this.sourceImage = ImageIO.read(imageFile);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from file", exc);
        }

        return this;
    }

    /**
     * Sets the source URL of the image.
     *
     * @param imageUrl the source URL of the image
     * @return the layer
     */
    public ImageLayer setImage(URL imageUrl) {
        this.imageUrl = imageUrl;
        try {
            this.sourceImage = ImageIO.read(imageUrl);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from URL", exc);
        }

        return this;
    }

    /**
     * Sets the Base64 encoding of the image.
     *
     * @param imageBase64 the Base64 encoding of the image
     * @return the layer
     */
    public ImageLayer setImage(String imageBase64) {
        this.imageBase64 = imageBase64;
        try {
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            this.sourceImage = ImageIO.read(inputStream);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot decode Base64 string as image", exc);
        }

        return this;
    }

    /**
     * Sets the source image from an existing {@link BufferedImage}.
     *
     * @param sourceImage the input image
     * @return the layer
     */
    public ImageLayer setImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        return this;
    }

    /**
     * Gets the scaling options.
     * @return the scaling options
     */
    public ScalingOptions getScalingOptions() {
        return this.scalingOptions;
    }

    /**
     * Gets the rotation options.
     * @return the rotation options
     */
    public RotationOptions getRotationOptions() {
        return rotationOptions;
    }

    /**
     * Gets the position options.
     * @return the position options
     */
    public PositionOptions getPositionOptions() {
        return this.positionOptions;
    }

    /**
     * Gets the blending options.
     * @return the blending options
     */
    public BlendingOptions getBlendingOptions() {
        return this.blendingOptions;
    }

    /**
     * Sets the scaling options.
     * @param scalingOptions the scaling options
     * @return the layer
     */
    public ImageLayer setScalingOptions(ScalingOptions scalingOptions) {
        this.scalingOptions = scalingOptions;
        return this;
    }

    /**
     * Sets the rotation options.
     * @param rotationOptions the rotation options
     * @return the layer
     */
    public ImageLayer setRotationOptions(RotationOptions rotationOptions) {
        this.rotationOptions = rotationOptions;
        return this;
    }

    /**
     * Sets the position options.
     * @param positionOptions the position options
     * @return the layer
     */
    public ImageLayer setPositionOptions(PositionOptions positionOptions) {
        this.positionOptions = positionOptions;
        return this;
    }

    /**
     * Sets the blending options.
     * @param blendingOptions the blending options
     * @return the layer
     */
    public ImageLayer setBlendingOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        this.checkStateConsistency();

        return this.applyOptionsStack(
                this.sourceImage,
                image,
                this.scalingOptions,
                this.rotationOptions,
                this.positionOptions,
                this.blendingOptions
        );
    }

    private void checkStateConsistency() {
        if (Stream.of(this.imageFile, this.imageUrl, this.imageBase64).allMatch(Objects::isNull)) {
            throw new IllegalStateException(
                    "One value between image file, image URL and image Base64 should not be null"
            );
        }

        if (Stream.of(this.imageFile, this.imageUrl, this.imageBase64).filter(Objects::nonNull).count() > 1) {
            throw new IllegalStateException(
                    "Only one value between image file, image URL and image Base64 can be specified"
            );
        }
    }
}
