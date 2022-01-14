package io.github.steromano87.pig4j.layers;

import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.RotationOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;

import java.awt.image.BufferedImage;

/**
 * Generic transformation layer that is applied to an image
 *
 * An image is composed as the superimposition of multiple layers, each of them adding an effect to the final image
 * (e.g. adding a blurring effect, some text blocks and so on).
 */
public interface Layer {
    /**
     * Applies the current layer to the image outputted by the previous layer
     *
     * @param image the previously outputted image
     * @return the image after being processed by the layer
     */
    BufferedImage apply(BufferedImage image);

    default BufferedImage applyOptionsStack(
            BufferedImage generatedImage,
            BufferedImage bgImage,
            ScalingOptions scalingOptions,
            RotationOptions rotationOptions,
            PositionOptions positionOptions,
            BlendingOptions blendingOptions
    ) {
        BufferedImage scaledAndRotated = rotationOptions.rotate(
                scalingOptions.scale(generatedImage)
        );

        positionOptions.overrideCanvasSize(bgImage.getWidth(), bgImage.getHeight());

        return blendingOptions.blend(
                positionOptions.translate(scaledAndRotated),
                bgImage
        );
    }
}
