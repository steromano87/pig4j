package com.github.steromano87.pig4j.layers.base;

import com.github.steromano87.pig4j.exceptions.ImageReadingException;
import com.github.steromano87.pig4j.layers.Layer;
import com.github.steromano87.pig4j.options.BlendingOptions;
import com.github.steromano87.pig4j.options.PositionOptions;
import com.github.steromano87.pig4j.options.ScalingOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Stream;

public class ImageLayer implements Layer {
    private BufferedImage sourceImage;

    private File imageFile;
    private URL imageUrl;
    private String imageBase64;

    private ScalingOptions scalingOptions = new ScalingOptions();
    private PositionOptions positionOptions = new PositionOptions();
    private BlendingOptions blendingOptions = new BlendingOptions();

    public ImageLayer setImageFile(File imageFile) {
        this.imageFile = imageFile;
        try {
            this.sourceImage = ImageIO.read(imageFile);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from file", exc);
        }

        return this;
    }

    public ImageLayer setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        try {
            this.sourceImage = ImageIO.read(imageUrl);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from URL", exc);
        }

        return this;
    }

    public ImageLayer setImageBase64(String imageBase64) {
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

    public ImageLayer setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        return this;
    }

    public ScalingOptions getScalingOptions() {
        return this.scalingOptions;
    }

    public PositionOptions getPositionOptions() {
        return this.positionOptions;
    }

    public BlendingOptions getBlendingOptions() {
        return this.blendingOptions;
    }

    public ImageLayer setScalingOptions(ScalingOptions scalingOptions) {
        this.scalingOptions = scalingOptions;
        return this;
    }

    public ImageLayer setPositionOptions(PositionOptions positionOptions) {
        this.positionOptions = positionOptions;
        return this;
    }

    public ImageLayer setBlendingOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        this.checkStateConsistency();
        return this.blendingOptions.apply(
                image,
                this.positionOptions.apply(
                        image,
                        this.scalingOptions.apply(this.sourceImage)
                )
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
