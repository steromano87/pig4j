package io.github.steromano87.pig4j;

import io.github.steromano87.pig4j.exceptions.ImageGenerationException;
import io.github.steromano87.pig4j.layers.base.SingleColorLayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

class ImageGeneratorTests {
    @Test
    void testSimpleInstantiation() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void testSimpleInstantiationWithBackgroundColor() {
        ImageGenerator generator = new ImageGenerator(640, 480, Color.BLACK);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void testSimpleInstantiationWithAlphaChannel() {
        ImageGenerator generator = new ImageGenerator(640, 480, null,true);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void testNoLayersException() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        Assertions.assertThrows(
                ImageGenerationException.class,
                generator::build
        );
    }

    @Test
    void testOutputAsJavaImage() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        SingleColorLayer layer = new SingleColorLayer();
        layer.setColor(Color.BLUE);
        generator.addLayer(layer);

        BufferedImage outputImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(BufferedImage.class, outputImage),
                () -> Assertions.assertEquals(640, outputImage.getWidth()),
                () -> Assertions.assertEquals(480, outputImage.getHeight()),
                () -> Assertions.assertFalse(outputImage.getColorModel().hasAlpha())
        );
    }

    @Test
    void testMultipleBuildInvocationsWithSameParameters() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        SingleColorLayer layer = new SingleColorLayer();
        layer.setColor(Color.BLUE);
        generator.addLayer(layer);

        generator.build();
        generator.build();

        BufferedImage outputImage = generator.toImage();

        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(BufferedImage.class, outputImage),
                () -> Assertions.assertEquals(640, outputImage.getWidth()),
                () -> Assertions.assertEquals(480, outputImage.getHeight()),
                () -> Assertions.assertFalse(outputImage.getColorModel().hasAlpha())
        );
    }

    @Test
    void testMultipleBuildInvocationsWithParametersChange() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        SingleColorLayer layer = new SingleColorLayer();
        layer.setColor(Color.BLUE);
        generator.addLayer(layer);

        generator.build();
        String blueOutputImageBase64 = generator.toBase64(ImageFormat.PNG);

        layer.setColor(Color.RED);
        generator.build();
        String redOutputImageBase64 = generator.toBase64(ImageFormat.PNG);

        Assertions.assertNotEquals(blueOutputImageBase64, redOutputImageBase64);
    }
}
