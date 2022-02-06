package io.github.steromano87.pig4j.options;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

class PositionOptionsTests {
    @Test
    void testNoOpTranslation() throws IOException {
        PositionOptions positionOptions = new PositionOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        BufferedImage outputImage = positionOptions.translate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testTranslationWithNoAutoResize() throws IOException {
        PositionOptions positionOptions = new PositionOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        positionOptions.setAutoResizeCanvas(false);
        positionOptions.setX(200);
        positionOptions.setY(100);

        BufferedImage outputImage = positionOptions.translate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testTranslationWithAutoResize() throws IOException {
        PositionOptions positionOptions = new PositionOptions();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        positionOptions.setAutoResizeCanvas(true);
        positionOptions.setX(200);
        positionOptions.setY(100);

        BufferedImage outputImage = positionOptions.translate(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() + 200, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() + 100, outputImage.getHeight())
        );
    }
}
