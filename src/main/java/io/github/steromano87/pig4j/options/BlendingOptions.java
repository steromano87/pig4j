package io.github.steromano87.pig4j.options;


import io.github.steromano87.pig4j.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Class that holds options used to blend two images together
 */
public class BlendingOptions {
    private float alpha = 1.0f;

    private Method method = Method.NORMAL;

    public BlendingOptions setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public BlendingOptions setMethod(Method method) {
        this.method = method;
        return this;
    }

    /**
     * Blends two images together
     *
     * @param bgImage background image
     * @param fgImage foreground image
     * @return the blended image
     */
    public BufferedImage blend(BufferedImage fgImage, BufferedImage bgImage) {
        if (fgImage.getHeight() != bgImage.getHeight() || fgImage.getWidth() != bgImage.getWidth()) {
            throw new ImageGenerationException(
                    String.format(
                            "Only image with same width and height can be blended; " +
                                    "foreground image: %d x %d, background image: %d x %d",
                            fgImage.getWidth(), fgImage.getHeight(),
                            bgImage.getWidth(), bgImage.getHeight()
                    )
            );
        }

        boolean hasTransparency = bgImage.getColorModel().hasAlpha();
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage outputImage = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), imageType);

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(bgImage, 0, 0, null);

        // TODO: set the various fusion options
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, this.alpha);
        graphics2D.setComposite(alphaComposite);
        graphics2D.drawImage(fgImage, 0, 0, null);
        graphics2D.dispose();

        return outputImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendingOptions that = (BlendingOptions) o;
        return Float.compare(that.alpha, alpha) == 0 &&
                method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, method);
    }

    /**
     * Blending method used while joining images
     *
     * WIP: currently only the NORMAL method is implicitly used
     */
    public enum Method {
        NORMAL,
        SUBTRACT
    }
}
