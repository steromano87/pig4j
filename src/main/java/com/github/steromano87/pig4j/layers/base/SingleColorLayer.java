package com.github.steromano87.pig4j.layers.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.github.steromano87.pig4j.layers.Layer;
import com.github.steromano87.pig4j.options.BlendingOptions;
import com.github.steromano87.pig4j.serialization.ColorDeserializer;
import com.github.steromano87.pig4j.serialization.ColorSerializer;

import java.awt.*;
import java.awt.image.BufferedImage;

@JsonDeserialize(as = Layer.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SingleColorLayer implements Layer {
    @JacksonXmlProperty(isAttribute = true)
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
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
        boolean hasTransparency = this.color.getAlpha() < 255;
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.setColor(this.color);
        graphics2D.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
        graphics2D.dispose();

        // Apply fusion options
        return this.blendingOptions.apply(image, outputImage);
    }
}
