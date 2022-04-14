package io.github.steromano87.pig4j.layers.sharpness;

import com.jhlabs.image.UnsharpFilter;
import io.github.steromano87.pig4j.Layer;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class SharpeningLayer implements Layer {
    @Setter
    private float amount = 0.5f;

    @Setter
    private int threshold = 1;

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        UnsharpFilter unsharpFilter = new UnsharpFilter();
        unsharpFilter.setThreshold(this.threshold);
        unsharpFilter.setAmount(this.amount);
        unsharpFilter.filter(image, outputImage);
        return outputImage;
    }
}
