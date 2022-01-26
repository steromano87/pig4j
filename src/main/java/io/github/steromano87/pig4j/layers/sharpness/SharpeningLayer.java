package io.github.steromano87.pig4j.layers.sharpness;

import com.jhlabs.image.UnsharpFilter;
import io.github.steromano87.pig4j.layers.Layer;

import java.awt.image.BufferedImage;

public class SharpeningLayer implements Layer {
    private float amount = 0.5f;
    private int threshold = 1;

    private final UnsharpFilter unsharpFilter = new UnsharpFilter();

    public SharpeningLayer setAmount(float amount) {
        this.amount = amount;
        this.unsharpFilter.setAmount(this.amount);
        return this;
    }

    public SharpeningLayer setThreshold(int threshold) {
        this.threshold = threshold;
        this.unsharpFilter.setThreshold(this.threshold);
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        this.unsharpFilter.filter(image, outputImage);
        return outputImage;
    }
}
