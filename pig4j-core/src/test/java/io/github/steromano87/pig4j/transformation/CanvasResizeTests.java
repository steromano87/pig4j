package io.github.steromano87.pig4j.transformation;

import io.github.steromano87.pig4j.Anchor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class CanvasResizeTests {
    private static final BufferedImage inputImage;

    static {
        try {
            inputImage = ImageIO.read(
                    Objects.requireNonNull(
                            CanvasResizeTests.class.getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testNoOpCanvasResize() {
        CanvasResize canvasResize = new CanvasResize(inputImage.getWidth(), inputImage.getHeight());
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }

    @Test
    void testEnlargeCanvasWithDefaultAnchor() {
        CanvasResize canvasResize = new CanvasResize(1000, 600);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1000, outputImage.getWidth()),
                () -> Assertions.assertEquals(600, outputImage.getHeight())
        );
    }

    @Test
    void testEnlargeCanvasWithMidCenterAnchor() {
        CanvasResize canvasResize = new CanvasResize(1000, 600, Anchor.MID_CENTER);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1000, outputImage.getWidth()),
                () -> Assertions.assertEquals(600, outputImage.getHeight())
        );
    }

    @Test
    void testEnlargeCanvasWithBottomRightAnchor() {
        CanvasResize canvasResize = new CanvasResize(1000, 600, Anchor.BOTTOM_RIGHT);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1000, outputImage.getWidth()),
                () -> Assertions.assertEquals(600, outputImage.getHeight())
        );
    }

    @Test
    void testShrinkCanvasWithDefaultAnchor() {
        CanvasResize canvasResize = new CanvasResize(400, 200);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(400, outputImage.getWidth()),
                () -> Assertions.assertEquals(200, outputImage.getHeight())
        );
    }

    @Test
    void testShrinkCanvasWithMidCenterAnchor() {
        CanvasResize canvasResize = new CanvasResize(400, 200, Anchor.MID_CENTER);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(400, outputImage.getWidth()),
                () -> Assertions.assertEquals(200, outputImage.getHeight())
        );
    }

    @Test
    void testShrinkCanvasWithBottomRightAnchor() {
        CanvasResize canvasResize = new CanvasResize(400, 200, Anchor.BOTTOM_RIGHT);
        BufferedImage outputImage = canvasResize.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(400, outputImage.getWidth()),
                () -> Assertions.assertEquals(200, outputImage.getHeight())
        );
    }
}
