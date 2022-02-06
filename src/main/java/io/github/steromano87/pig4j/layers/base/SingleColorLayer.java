package io.github.steromano87.pig4j.layers.base;

import io.github.steromano87.pig4j.layers.Layer;
import io.github.steromano87.pig4j.options.BlendingOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class SingleColorLayer implements Layer {
    private Color color;

    private BlendingOptions blendingOptions = new BlendingOptions();

    public SingleColorLayer setColor(Color color) {
        this.color = color;
        return this;
    }

    public SingleColorLayer setFusionOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        if (Objects.isNull(this.color)) {
            throw new IllegalStateException("No color has been set");
        }

        boolean hasTransparency = this.color.getAlpha() < 255;
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.setColor(this.color);
        graphics2D.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
        graphics2D.dispose();

        // Apply fusion options
        return this.blendingOptions.blend(outputImage, image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleColorLayer that = (SingleColorLayer) o;
        return Objects.equals(color, that.color) && blendingOptions.equals(that.blendingOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, blendingOptions);
    }
}
