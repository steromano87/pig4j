package io.github.steromano87.pig4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class LayerPipelineTests {
    @Test
    void testHashCodeChangesWhenLayerChanges() {
        LayerPipeline pipeline = new LayerPipeline();
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(1));

        pipeline.addLayer(layer);
        int firstHashCode = pipeline.hashCode();

        layer.setReturnDelay(Duration.ofMillis(2));
        int secondHashCode = pipeline.hashCode();

        Assertions.assertNotEquals(firstHashCode, secondHashCode);
    }

    @Test
    void testHashCodeChangesWhenLayerIsAdded() {
        LayerPipeline pipeline = new LayerPipeline();
        NoOpLayer firstLayer = new NoOpLayer();
        firstLayer.setReturnDelay(Duration.ofMillis(1));
        pipeline.addLayer(firstLayer);
        int firstHashCode = pipeline.hashCode();

        NoOpLayer secondLayer = new NoOpLayer();
        secondLayer.setReturnDelay(Duration.ofMillis(2));
        pipeline.addLayer(secondLayer);
        int secondHashCode = pipeline.hashCode();

        Assertions.assertNotEquals(firstHashCode, secondHashCode);
    }
}
