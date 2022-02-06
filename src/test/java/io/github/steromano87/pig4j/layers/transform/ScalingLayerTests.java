package io.github.steromano87.pig4j.layers.transform;

import io.github.steromano87.pig4j.ImageGenerator;
import io.github.steromano87.pig4j.layers.base.ImageLayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScalingLayerTests {
    @Test
    void testEnlargementWithScaleUsingAutoAlgorithm() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        ScalingLayer scalingLayer = new ScalingLayer();
        scalingLayer.setScale(2.0);
        generator.addLayer(scalingLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(1280, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(800, generatedImage.getHeight(), "Generated image height mismatch")
        );
    }
}
