package io.github.steromano87.pig4j.layers;

import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.RotationOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;

import java.awt.image.BufferedImage;

/**
 * Generic transformation layer that is applied to an image.
 * <p>
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

    /**
     * Applies the options stack to the generated image.
     * <p>
     * The stack is always applied in the following order:
     * <ol>
     *     <li>ScalingOptions</li>
     *     <li>RotationOptions</li>
     *     <li>PositionOptions</li>
     *     <li>BlendingOptions</li>
     * </ol>
     *
     * @param generatedImage the input image
     * @param bgImage the image onto which the generated image should be rendered
     * @param scalingOptions the scaling options
     * @param rotationOptions the rotation options
     * @param positionOptions the position options
     * @param blendingOptions the blending options
     * @return the combined image
     */
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
