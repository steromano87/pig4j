package io.github.steromano87.pig4j.options;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class RotationOptionsTests {
    @Test
    void testNoOpZeroDegreesRotation() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testNoOp360DegreesRotation() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        rotationOptions.setAngle(360);
        BufferedImage outputImage = rotationOptions.rotate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testRotationWithNoAutoResize() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
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
    void testRotationWithAutoResize() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
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
    void testRotationWithNegativeAngles() throws IOException {
        RotationOptions rotationOptions = new RotationOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
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
