package io.github.steromano87.pig4j.options;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class ScalingOptionsTests {
    @Test
    void testNoOpScaling() throws IOException {
        ScalingOptions scalingOptions = new ScalingOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        BufferedImage outputImage = scalingOptions.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testEnlargementWithAutoAlgorithmScaling() throws IOException {
        ScalingOptions scalingOptions = new ScalingOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        scalingOptions.setScalingAlgorithm(ScalingOptions.Algorithm.AUTO);
        scalingOptions.setScale(2.0);

        BufferedImage outputImage = scalingOptions.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 2, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 2, outputImage.getHeight())
        );
    }

    @Test
    void testShrinkingWithAutoAlgorithmScaling() throws IOException {
        ScalingOptions scalingOptions = new ScalingOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        scalingOptions.setScalingAlgorithm(ScalingOptions.Algorithm.AUTO);
        scalingOptions.setScale(0.6);

        BufferedImage outputImage = scalingOptions.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 0.6, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 0.6, outputImage.getHeight())
        );
    }

    @Test
    void testNonUniformScaling() throws IOException {
        ScalingOptions scalingOptions = new ScalingOptions();
        BufferedImage inputImage = ImageIO.read(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                )
        );

        scalingOptions.setUniformScaling(false);
        scalingOptions.setScaleX(0.8);
        scalingOptions.setScaleY(1.4);

        BufferedImage outputImage = scalingOptions.scale(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() * 0.8, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() * 1.4, outputImage.getHeight())
        );
    }

    @Test
    void testExceptionRaisedWhenSettingSingleAxisScaleWithUniformScaling() {
        ScalingOptions scalingOptions = new ScalingOptions();
        scalingOptions.setUniformScaling(true);

        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalStateException.class, () -> scalingOptions.setScaleX(1.5)),
                () -> Assertions.assertThrows(IllegalStateException.class, () -> scalingOptions.setScaleY(1.5))
        );
    }
}
