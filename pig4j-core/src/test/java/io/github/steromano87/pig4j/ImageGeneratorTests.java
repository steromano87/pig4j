package io.github.steromano87.pig4j;

import io.github.steromano87.pig4j.exceptions.ImageGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;

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
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(1));
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
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(1));
        generator.addLayer(layer);

        generator.build();
        BufferedImage firstOutputImage = generator.toImage();

        generator.build();
        BufferedImage secondOutputImage = generator.toImage();

        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(BufferedImage.class, firstOutputImage),
                () -> Assertions.assertEquals(640, firstOutputImage.getWidth()),
                () -> Assertions.assertEquals(480, firstOutputImage.getHeight()),
                () -> Assertions.assertFalse(firstOutputImage.getColorModel().hasAlpha()),
                () -> Assertions.assertEquals(firstOutputImage, secondOutputImage)
        );
    }

    @Test
    void testMultipleBuildInvocationsWithParametersChange() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(1));
        generator.addLayer(layer);

        generator.build();
        String firstOutputImageBase64 = generator.toBase64(ImageFormat.PNG);

        layer.setReturnDelay(Duration.ofMillis(2));
        generator.build();
        String secondOutputImageBase64 = generator.toBase64(ImageFormat.PNG);

        Assertions.assertEquals(firstOutputImageBase64, secondOutputImageBase64);
    }

    @Test
    void testMultipleBuildInvocationWithCacheEnabled() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        generator.setForcedCacheInvalidation(false);
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(200));
        generator.addLayer(layer);

        Instant start = Instant.now();
        generator.build();
        Instant end = Instant.now();

        Assertions.assertTrue(Duration.between(start, end).compareTo(Duration.ofMillis(200)) >= 0);

        Instant secondStart = Instant.now();
        generator.build();
        Instant secondEnd = Instant.now();

        Assertions.assertTrue(Duration.between(secondStart, secondEnd).compareTo(Duration.ofMillis(200)) < 0);
    }

    @Test
    void testMultipleBuildInvocationWithCacheDisabled() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        generator.setForcedCacheInvalidation(true);
        NoOpLayer layer = new NoOpLayer();
        layer.setReturnDelay(Duration.ofMillis(200));
        generator.addLayer(layer);

        Instant start = Instant.now();
        generator.build();
        Instant end = Instant.now();

        Assertions.assertTrue(Duration.between(start, end).compareTo(Duration.ofMillis(200)) >= 0);

        Instant secondStart = Instant.now();
        generator.build();
        Instant secondEnd = Instant.now();

        Assertions.assertTrue(Duration.between(secondStart, secondEnd).compareTo(Duration.ofMillis(200)) >= 0);
    }
}
