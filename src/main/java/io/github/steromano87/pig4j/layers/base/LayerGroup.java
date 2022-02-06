package io.github.steromano87.pig4j.layers.base;

import io.github.steromano87.pig4j.layers.Layer;
import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.RotationOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;
import lombok.EqualsAndHashCode;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class LayerGroup implements Layer {
    private List<Layer> layers = new ArrayList<>();

    private ScalingOptions scalingOptions = new ScalingOptions();
    private RotationOptions rotationOptions = new RotationOptions();
    private PositionOptions positionOptions = new PositionOptions();
    private BlendingOptions blendingOptions = new BlendingOptions();

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public ScalingOptions getScalingOptions() {
        return scalingOptions;
    }

    public RotationOptions getRotationOptions() {
        return rotationOptions;
    }

    public PositionOptions getPositionOptions() {
        return positionOptions;
    }

    public BlendingOptions getBlendingOptions() {
        return blendingOptions;
    }

    public LayerGroup setScalingOptions(ScalingOptions scalingOptions) {
        this.scalingOptions = scalingOptions;
        return this;
    }

    public LayerGroup setRotationOptions(RotationOptions rotationOptions) {
        this.rotationOptions = rotationOptions;
        return this;
    }

    public LayerGroup setPositionOptions(PositionOptions positionOptions) {
        this.positionOptions = positionOptions;
        return this;
    }

    public LayerGroup setBlendingOptions(BlendingOptions blendingOptions) {
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

        return this.applyOptionsStack(
                layerCanvas,
                image,
                this.scalingOptions,
                this.rotationOptions,
                this.positionOptions,
                this.blendingOptions
        );
    }
}
