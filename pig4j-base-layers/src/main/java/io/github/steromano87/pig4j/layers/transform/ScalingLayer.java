package io.github.steromano87.pig4j.layers.transform;

import io.github.steromano87.pig4j.Layer;
import io.github.steromano87.pig4j.options.ScalingOptions;
import lombok.EqualsAndHashCode;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class ScalingLayer implements Layer {
    private final ScalingOptions scalingOptions = new ScalingOptions();

    public ScalingLayer setWidth(int width) {
        this.scalingOptions.setWidth(width);
        return this;
    }

    public ScalingLayer setHeight(int height) {
        this.scalingOptions.setHeight(height);
        return this;
    }

    public ScalingLayer setScaleX(double scale) {
        this.scalingOptions.setScaleX(scale);
        return this;
    }

    public ScalingLayer setScaleY(double scale) {
        this.scalingOptions.setScaleY(scale);
        return this;
    }

    public ScalingLayer setScale(double scale) {
        this.scalingOptions.setScale(scale);
        return this;
    }

    public ScalingLayer setUniformScaling(boolean uniformScaling) {
        this.scalingOptions.setUniformScaling(uniformScaling);
        return this;
    }

    public ScalingLayer setScalingAlgorithm(ScalingOptions.Algorithm algorithm) {
        this.scalingOptions.setScalingAlgorithm(algorithm);
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return this.scalingOptions.scale(image);
    }
}
