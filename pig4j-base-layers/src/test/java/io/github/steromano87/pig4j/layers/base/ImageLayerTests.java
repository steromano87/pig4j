package io.github.steromano87.pig4j.layers.base;

import io.github.steromano87.pig4j.ImageGenerator;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class ImageLayerTests {
    @Test
    void testImageLayerFromFile() throws IOException {
        ImageGenerator generator = new ImageGenerator(640, 400);

        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());

        generator.addLayer(imageLayer);
        BufferedImage generatedImage = generator.build().toImage();
        BufferedImage originalImage = ImageIO.read(imagePath.toFile());

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertEquals(
                        originalImage.getColorModel().hasAlpha(),
                        generatedImage.getColorModel().hasAlpha(),
                        "Alpha channel mismatch")
        );
    }

    @Test
    void testMultipleImageLayersFromFile() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        // Add first image
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        // Add second image
        ImageLayer secondImageLayer = new ImageLayer();
        Path secondImagePath = Paths.get("src/test/resources/common", "trollface.png");
        secondImageLayer.setImage(secondImagePath.toFile());
        generator.addLayer(secondImageLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertFalse(generatedImage.getColorModel().hasAlpha(), "Final image should have alpha channel")
        );
    }

    @Test
    void testCenteredImagePositionGeneration() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        // Add first image
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        // Add second image
        ImageLayer secondImageLayer = new ImageLayer();
        Path secondImagePath = Paths.get("src/test/resources/common", "trollface.png");
        secondImageLayer.setImage(secondImagePath.toFile());

        PositionOptions positionOptions = new PositionOptions();
        positionOptions.setAnchor(PositionOptions.Anchor.MID_CENTER);
        positionOptions.setX(320);
        positionOptions.setY(200);
        secondImageLayer.setPositionOptions(positionOptions);

        generator.addLayer(secondImageLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertFalse(generatedImage.getColorModel().hasAlpha(), "Final image should have alpha channel")
        );
    }

    @Test
    void testOffCanvasImageRendering() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        // Add first image
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        // Add second image
        ImageLayer secondImageLayer = new ImageLayer();
        Path secondImagePath = Paths.get("src/test/resources/common", "trollface.png");
        secondImageLayer.setImage(secondImagePath.toFile());

        PositionOptions positionOptions = new PositionOptions();
        positionOptions.setAnchor(PositionOptions.Anchor.MID_CENTER);
        positionOptions.setX(-50);
        positionOptions.setY(200);
        secondImageLayer.setPositionOptions(positionOptions);

        generator.addLayer(secondImageLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertFalse(generatedImage.getColorModel().hasAlpha(), "Final image should have alpha channel")
        );
    }

    @Test
    void testImageResizeWithWidth() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        // Add first image
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        // Add second image
        ImageLayer secondImageLayer = new ImageLayer();
        Path secondImagePath = Paths.get("src/test/resources/common", "trollface.png");
        secondImageLayer.setImage(secondImagePath.toFile());

        PositionOptions positionOptions = new PositionOptions();
        positionOptions.setAnchor(PositionOptions.Anchor.MID_CENTER);
        positionOptions.setX(320);
        positionOptions.setY(200);
        secondImageLayer.setPositionOptions(positionOptions);

        ScalingOptions scalingOptions = new ScalingOptions();
        scalingOptions.setWidth(320);
        secondImageLayer.setScalingOptions(scalingOptions);

        generator.addLayer(secondImageLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertFalse(generatedImage.getColorModel().hasAlpha(), "Final image should have alpha channel")
        );
    }

    @Test
    void testImageResizeWithScale() {
        ImageGenerator generator = new ImageGenerator(640, 400);

        // Add first image
        ImageLayer imageLayer = new ImageLayer();
        Path imagePath = Paths.get("src/test/resources/common", "landscape_640_400.jpg");
        imageLayer.setImage(imagePath.toFile());
        generator.addLayer(imageLayer);

        // Add second image
        ImageLayer secondImageLayer = new ImageLayer();
        Path secondImagePath = Paths.get("src/test/resources/common", "trollface.png");
        secondImageLayer.setImage(secondImagePath.toFile());

        PositionOptions positionOptions = new PositionOptions();
        positionOptions.setAnchor(PositionOptions.Anchor.MID_CENTER);
        positionOptions.setX(320);
        positionOptions.setY(200);
        secondImageLayer.setPositionOptions(positionOptions);

        ScalingOptions scalingOptions = new ScalingOptions();
        scalingOptions.setScale(1.5);
        secondImageLayer.setScalingOptions(scalingOptions);

        generator.addLayer(secondImageLayer);

        BufferedImage generatedImage = generator.build().toImage();

        Assertions.assertAll(
                () -> Assertions.assertEquals(640, generatedImage.getWidth(), "Generated image width mismatch"),
                () -> Assertions.assertEquals(400, generatedImage.getHeight(), "Generated image height mismatch"),
                () -> Assertions.assertFalse(generatedImage.getColorModel().hasAlpha(), "Final image should have alpha channel")
        );
    }
}
