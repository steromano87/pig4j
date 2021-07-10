package com.github.steromano87.pig4j.layers;

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
}
