package io.github.steromano87.pig4j.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

class ScalerTests {
    @Test
    void NoOpScalingTest() throws IOException {
        Scaler scaler = new Scaler();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        BufferedImage outputImage = scaler.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void EnlargementWithAutoAlgorithmScalingTest() throws IOException {
        Scaler scaler = new Scaler();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        scaler.setScalingAlgorithm(ScalingAlgorithm.AUTO);
        scaler.setScale(2.0);

        BufferedImage outputImage = scaler.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 2, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 2, outputImage.getHeight())
        );
    }

    @Test
    void ShrinkingWithAutoAlgorithmScalingTest() throws IOException {
        Scaler scaler = new Scaler();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        scaler.setScalingAlgorithm(ScalingAlgorithm.AUTO);
        scaler.setScale(0.6);

        BufferedImage outputImage = scaler.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 0.6, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 0.6, outputImage.getHeight())
        );
    }

    @Test
    void NonUniformScalingTest() throws IOException {
        Scaler scaler = new Scaler();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        scaler.setUniformScaling(false);
        scaler.setScaleX(0.8);
        scaler.setScaleY(1.4);

        BufferedImage outputImage = scaler.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 0.8, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 1.4, outputImage.getHeight())
        );
    }

    @Test
    void ExceptionRaisedWhenSettingSingleAxisScaleWithUniformScalingTest() {
        Scaler scaler = new Scaler();
        scaler.setUniformScaling(true);

        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalStateException.class, () -> scaler.setScaleX(1.5)),
                () -> Assertions.assertThrows(IllegalStateException.class, () -> scaler.setScaleY(1.5))
        );
    }
}
