package io.github.steromano87.pig4j.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

class RotatorTests {
    @Test
    void NoOpZeroDegreesRotationTest() throws IOException {
        Rotator rotator = new Rotator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        BufferedImage outputImage = rotator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void NoOp360DegreesRotationTest() throws IOException {
        Rotator rotator = new Rotator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        rotator.setAngle(360);
        BufferedImage outputImage = rotator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void RotationWithNoAutoResizeTest() throws IOException {
        Rotator rotator = new Rotator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotator.setAngle(30);
        rotator.setAutoResizeCanvas(false);

        BufferedImage outputImage = rotator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void RotationWithAutoResizeTest() throws IOException {
        Rotator rotator = new Rotator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotator.setAngle(30);
        rotator.setAutoResizeCanvas(true);

        BufferedImage outputImage = rotator.transform(inputImage);

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
        Rotator rotator = new Rotator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        rotator.setAngle(-30);
        rotator.setAutoResizeCanvas(true);

        BufferedImage outputImage = rotator.transform(inputImage);

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
