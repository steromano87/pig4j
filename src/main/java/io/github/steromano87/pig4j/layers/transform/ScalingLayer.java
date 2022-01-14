package io.github.steromano87.pig4j.layers.transform;

import io.github.steromano87.pig4j.layers.Layer;
import io.github.steromano87.pig4j.tools.Scaler;
import io.github.steromano87.pig4j.tools.ScalingAlgorithm;

import java.awt.image.BufferedImage;


public class ScalingLayer implements Layer {
    private final Scaler scaler = new Scaler();

    public ScalingLayer setWidth(int width) {
        this.scaler.setWidth(width);
        return this;
    }

    public ScalingLayer setHeight(int height) {
        this.scaler.setHeight(height);
        return this;
    }

    public ScalingLayer setScaleX(double scale) {
        this.scaler.setScaleX(scale);
        return this;
    }

    public ScalingLayer setScaleY(double scale) {
        this.scaler.setScaleY(scale);
        return this;
    }

    public ScalingLayer setScale(double scale) {
        this.scaler.setScale(scale);
        return this;
    }

    public ScalingLayer setUniformScaling(boolean uniformScaling) {
        this.scaler.setUniformScaling(uniformScaling);
        return this;
    }

    public ScalingLayer setScalingAlgorithm(ScalingAlgorithm algorithm) {
        this.scaler.setScalingAlgorithm(algorithm);
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        return this.scaler.scale(image);
    }
}
