package io.github.steromano87.pig4j.layers.sharpness;

import com.jhlabs.image.GaussianFilter;
import io.github.steromano87.pig4j.layers.Layer;

import java.awt.image.BufferedImage;

public class GaussianBlurLayer implements Layer {
    private int radius = 3;
    private final GaussianFilter gaussianFilter = new GaussianFilter(this.radius);

    public GaussianBlurLayer setRadius(int radius) {
        this.radius = radius;
        this.gaussianFilter.setRadius(this.radius);
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        if (this.isNoOp()) {
            return image;
        }

        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        this.gaussianFilter.filter(image, outputImage);
        return outputImage;
    }

    private boolean isNoOp() {
        return this.radius == 1;
    }
}
