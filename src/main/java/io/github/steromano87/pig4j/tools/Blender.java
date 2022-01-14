package io.github.steromano87.pig4j.tools;

import io.github.steromano87.pig4j.exceptions.ImageGenerationException;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blender {
    private float alpha = 1.0f;

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

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
}
