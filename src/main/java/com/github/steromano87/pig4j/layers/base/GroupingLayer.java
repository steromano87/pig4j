package com.github.steromano87.pig4j.layers.base;

import com.github.steromano87.pig4j.layers.Layer;
import com.github.steromano87.pig4j.options.BlendingOptions;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class GroupingLayer implements Layer {
    private List<Layer> layers = new ArrayList<>();

    private BlendingOptions blendingOptions = new BlendingOptions();

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public GroupingLayer setBlendingOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        boolean hasTransparency = image.getColorModel().hasAlpha();
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage layerCanvas = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        for (Layer layer : this.layers) {
            layer.apply(layerCanvas);
        }

        return this.blendingOptions.apply(image, layerCanvas);
    }
}
