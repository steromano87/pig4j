package io.github.steromano87.pig4j;


import io.github.steromano87.pig4j.layers.LayerPipeline;
import io.github.steromano87.pig4j.layers.base.SingleColorLayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class LayerPipelineTests {
    @Test
    void testHashCodeChangesWhenLayerChanges() {
        LayerPipeline pipeline = new LayerPipeline();
        SingleColorLayer layer = new SingleColorLayer();
        layer.setColor(Color.BLUE);

        pipeline.addLayer(layer);
        int firstHashCode = pipeline.hashCode();

        layer.setColor(Color.GREEN);
        int secondHashCode = pipeline.hashCode();

        Assertions.assertNotEquals(firstHashCode, secondHashCode);
    }

    @Test
    void testHashCodeChangesWhenLayerIsAdded() {
        LayerPipeline pipeline = new LayerPipeline();
        SingleColorLayer firstLayer = new SingleColorLayer();
        firstLayer.setColor(Color.BLUE);
        pipeline.addLayer(firstLayer);
        int firstHashCode = pipeline.hashCode();

        SingleColorLayer secondLayer = new SingleColorLayer();
        secondLayer.setColor(Color.RED);
        pipeline.addLayer(secondLayer);
        int secondHashCode = pipeline.hashCode();

        Assertions.assertNotEquals(firstHashCode, secondHashCode);
    }
}
