package io.github.steromano87.pig4j.options;


import io.github.steromano87.pig4j.exceptions.ImageGenerationException;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class that holds options used to blend two images together.
 */
@EqualsAndHashCode
public class BlendingOptions {
    @Setter
    private float alpha = 1.0f;

    @Setter
    private Method method = Method.NORMAL;

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

    /**
     * Blending method used while joining images
     * <p>
     * WIP: currently only the NORMAL method is implicitly used
     */
    public enum Method {
        NORMAL,
        SUBTRACT
    }
}
