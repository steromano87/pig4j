package io.github.steromano87.pig4j.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

class TranslatorTests {
    @Test
    void NoOpTranslationTest() throws IOException {
        Translator translator = new Translator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );

        BufferedImage outputImage = translator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void TranslationWithNoAutoResizeTest() throws IOException {
        Translator translator = new Translator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        translator.setAutoResizeCanvas(false);
        translator.setX(200);
        translator.setY(100);

        BufferedImage outputImage = translator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void TranslationWithAutoResizeTest() throws IOException {
        Translator translator = new Translator();
        BufferedImage inputImage = ImageIO.read(
                Paths.get("src/test/resources/common", "landscape_640_400.jpg").toFile()
        );
        translator.setAutoResizeCanvas(true);
        translator.setX(200);
        translator.setY(100);

        BufferedImage outputImage = translator.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth() + 200, outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight() + 100, outputImage.getHeight())
        );
    }
}
