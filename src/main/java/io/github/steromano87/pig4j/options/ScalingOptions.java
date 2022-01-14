package io.github.steromano87.pig4j.options;

import io.github.steromano87.pig4j.tools.Scaler;
import io.github.steromano87.pig4j.tools.ScalingAlgorithm;

import java.awt.image.BufferedImage;

/**
 * Class that holds the scaling options used when merging two images together
 *
 * The option defines how an image with an arbitrary size is fitted on an existing canvas
 *
 * @deprecated use {@link io.github.steromano87.pig4j.layers.transform.ScalingLayer} instead
 */
@Deprecated
public class ScalingOptions {
    private final Scaler scaler = new Scaler();

    public ScalingOptions setWidth(int width) {
        this.scaler.setWidth(width);
        return this;
    }

    public ScalingOptions setHeight(int height) {
        this.scaler.setHeight(height);
        return this;
    }

    public ScalingOptions setScaleX(double scale) {
        this.scaler.setScaleX(scale);
        return this;
    }

    public ScalingOptions setScaleY(double scale) {
        this.scaler.setScaleY(scale);
        return this;
    }

    public ScalingOptions setScale(double scale) {
        this.scaler.setScale(scale);
        return this;
    }

    public ScalingOptions setAlgorithm(ScalingAlgorithm algorithm) {
        this.scaler.setScalingAlgorithm(algorithm);
        return this;
    }

    public BufferedImage apply(BufferedImage image) {
        return this.scaler.scale(image);
    }
}
