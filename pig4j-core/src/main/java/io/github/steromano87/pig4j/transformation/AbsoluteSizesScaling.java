package io.github.steromano87.pig4j.transformation;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class AbsoluteSizesScaling extends Scaling {
    private final Integer width;
    private final Integer height;

    public AbsoluteSizesScaling(Integer width, Integer height) {
        if (Objects.isNull(width) && Objects.isNull(height)) {
            throw new IllegalArgumentException("At least one value between width and height has to be non-null");
        }

        this.width = width;
        this.height = height;
        this.uniformScaling = Objects.isNull(this.width) ^ Objects.isNull(this.height);
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        // If the scaler is working in no-op mode, immediately return the original image without further elaboration
        if (this.isNoOp(image)) {
            return image;
        }

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int finalWidth = this.calculateFinalWidth(originalWidth, originalHeight);
        int finalHeight = this.calculateFinalHeight(originalWidth, originalHeight);

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
        return (input.getWidth() == this.width && input.getHeight() == this.height);
    }

    @Override
    protected boolean isEnlargement(BufferedImage image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        return this.calculateFinalWidth(originalWidth, originalHeight) > originalWidth ||
                this.calculateFinalHeight(originalWidth, originalHeight) > originalHeight;
    }

    private int calculateFinalWidth(int originalWidth, int originalHeight) {
        if (Objects.nonNull(this.width)) {
            return this.width;
        } else {
            return Math.toIntExact(
                    Math.round(((double) this.height / originalHeight) * originalWidth)
            );
        }
    }

    private int calculateFinalHeight(int originalWidth, int originalHeight) {
        if (Objects.nonNull(this.height)) {
            return this.height;
        } else {
            return Math.toIntExact(
                    Math.round(((double) this.width / originalWidth) * originalHeight)
            );
        }
    }
}
