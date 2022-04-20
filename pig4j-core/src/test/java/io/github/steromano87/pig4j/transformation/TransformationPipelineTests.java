package io.github.steromano87.pig4j.transformation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class TransformationPipelineTests {
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
    void testNoOpWhenNoTransformationIsAdded() {
        TransformationPipeline pipeline = new TransformationPipeline();
        BufferedImage outputImage = pipeline.transform(inputImage);

        Assertions.assertAll(
                () -> Assertions.assertEquals(inputImage.getWidth(), outputImage.getWidth()),
                () -> Assertions.assertEquals(inputImage.getHeight(), outputImage.getHeight())
        );
    }
}
