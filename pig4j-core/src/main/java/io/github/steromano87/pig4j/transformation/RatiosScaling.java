package io.github.steromano87.pig4j.transformation;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class RatiosScaling extends Scaling {
    private final Double scaleX;
    private final Double scaleY;

    public RatiosScaling(double scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.uniformScaling = true;
    }

    public RatiosScaling(Double scaleX, Double scaleY) {
        if (Objects.isNull(scaleX) && Objects.isNull(scaleY)) {
            throw new IllegalArgumentException("At least one value between X scale and Y scale has to be non-null");
        }

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.uniformScaling = Objects.isNull(this.scaleX) ^ Objects.isNull(this.scaleY);
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        // If the scaler is working in no-op mode, immediately return the original image without further elaboration
        if (this.isNoOp(image)) {
            return image;
        }

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int finalWidth = this.calculateFinalWidth(originalWidth);
        int finalHeight = this.calculateFinalHeight(originalHeight);

        BufferedImage resizedImage = new BufferedImage(
                finalWidth,
                finalHeight,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHints(this.calculateScalingRenderingHints(image));
        graphics2D.drawImage(image,
                0,
                0,
                finalWidth,
                finalHeight,
                null
        );
        graphics2D.dispose();

        return resizedImage;
    }

    private boolean isNoOp(BufferedImage input) {
        return (this.scaleX == 1.0 && this.scaleY == 1.0);
    }

    @Override
    protected boolean isEnlargement(BufferedImage image) {
        return this.scaleX > 1.0 || this.scaleY > 1.0;
    }

    private int calculateFinalWidth(int originalWidth) {
        double calculatedScale = Objects.isNull(this.scaleX) ? this.scaleY : this.scaleX;
        return Math.toIntExact(Math.round((double) originalWidth * calculatedScale));
    }

    private int calculateFinalHeight(int originalHeight) {
        double calculatedScale = Objects.isNull(this.scaleY) ? this.scaleX : this.scaleY;
        return Math.toIntExact(Math.round((double) originalHeight * calculatedScale));
    }
}
