package io.github.steromano87.pig4j.transformation;

import io.github.steromano87.pig4j.Anchor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
@EqualsAndHashCode
public class CanvasResize implements Transformation {
    private final int width;
    private final int height;
    private final Anchor anchor;

    public CanvasResize(int width, int height) {
        this(width, height, Anchor.TOP_LEFT);
    }

    public CanvasResize(int width, int height, Anchor anchor) {
        this.width = width;
        this.height = height;
        this.anchor = anchor;
    }

    @Override
    public BufferedImage transform(BufferedImage initialImage) {
        if (this.isNoOp(initialImage)) {
            return initialImage;
        }

        int initialImageDrawingX = this.calculateInitialImageDrawingX(initialImage);
        int initialImageDrawingY = this.calculateInitialImageDrawingY(initialImage);

        BufferedImage outputImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(initialImage, initialImageDrawingX, initialImageDrawingY, null);
        graphics2D.dispose();

        return outputImage;
    }

    private boolean isNoOp(BufferedImage image) {
        return this.width == image.getWidth() && this.height == image.getHeight();
    }

    private int calculateInitialImageDrawingX(BufferedImage image) {
        return this.anchor.translateToTopLeftX(image, Math.toIntExact(
                Math.round(this.width * this.anchor.getHorizontalRelativePosition())
        ));
    }

    private int calculateInitialImageDrawingY(BufferedImage image) {
        return this.anchor.translateToTopLeftY(image, Math.toIntExact(
                Math.round(this.height * this.anchor.getVerticalRelativePosition())
        ));
    }
}
