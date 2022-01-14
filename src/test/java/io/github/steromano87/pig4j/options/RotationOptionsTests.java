package io.github.steromano87.pig4j.options;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

class RotationOptionsTests {
    @Test
    void NoOpZeroDegreesRotationTest() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void NoOp360DegreesRotationTest() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        rotationOptions.setAngle(360);
        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void RotationWithNoAutoResizeTest() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotationOptions.setAngle(30);
        rotationOptions.setAutoResizeCanvas(false);

        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void RotationWithAutoResizeTest() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotationOptions.setAngle(30);
        rotationOptions.setAutoResizeCanvas(true);

        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        double sin = Math.abs(Math.sin(Math.toRadians(30)));
        double cos = Math.abs(Math.cos(Math.toRadians(30)));

        int expectedImageWidth = (int) Math.floor(
                inputImage.getWidth() * cos + inputImage.getHeight() * sin
        );

        int expectedImageHeight = (int) Math.floor(
                inputImage.getWidth() * sin + inputImage.getHeight() * cos
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedImageWidth, outputImage.getWidth()),
                () -> Assertions.assertEquals(expectedImageHeight, outputImage.getHeight())
        );
    }

    @Test
    void RotationWithNegativeAnglesTest() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotationOptions.setAngle(-30);
        rotationOptions.setAutoResizeCanvas(true);

        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        double sin = Math.abs(Math.sin(Math.toRadians(-30)));
        double cos = Math.abs(Math.cos(Math.toRadians(-30)));

        int expectedImageWidth = (int) Math.floor(
                inputImage.getWidth() * cos + inputImage.getHeight() * sin
        );

        int expectedImageHeight = (int) Math.floor(
                inputImage.getWidth() * sin + inputImage.getHeight() * cos
        );

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedImageWidth, outputImage.getWidth()),
                () -> Assertions.assertEquals(expectedImageHeight, outputImage.getHeight())
        );
    }
}
