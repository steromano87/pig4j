package io.github.steromano87.pig4j.layers.sharpness;

import com.jhlabs.image.GaussianFilter;
import io.github.steromano87.pig4j.layers.Layer;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class GaussianBlurLayer implements Layer {
    @Setter
    private int radius = 3;

    @Override
    public BufferedImage apply(BufferedImage image) {
        if (this.isNoOp()) {
            return image;
        }

        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        GaussianFilter gaussianFilter = new GaussianFilter();
        gaussianFilter.setRadius(this.radius);
        gaussianFilter.filter(image, outputImage);
        return outputImage;
    }

    private boolean isNoOp() {
        return this.radius == 1;
    }
}
