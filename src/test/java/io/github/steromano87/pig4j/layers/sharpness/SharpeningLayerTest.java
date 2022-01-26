package io.github.steromano87.pig4j.layers.sharpness;

import io.github.steromano87.pig4j.ImageGenerator;
import io.github.steromano87.pig4j.layers.base.ImageLayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class SharpeningLayerTest {
    @Test
    void testSharpeningLayerWithDefaultParameters() throws IOException {
        ImageGenerator generator = new ImageGenerator(640, 400);
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImageFile(imagePath.toFile());
        generator.addLayer(imageLayer);

        SharpeningLayer sharpeningLayer = new SharpeningLayer();
        generator.addLayer(sharpeningLayer);

        BufferedImage generatedImage = generator.build().toImage();
        BufferedImage originalImage = ImageIO.read(imagePath.toFile());

        Assertions.assertAll(
                () -> Assertions.assertEquals(originalImage.getWidth(), generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(originalImage.getHeight(), generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertEquals(
                        originalImage.getColorModel().hasAlpha(),
                        generatedImage.getColorModel().hasAlpha(),
                        "Alpha channel mismatch")
        );
    }
}
