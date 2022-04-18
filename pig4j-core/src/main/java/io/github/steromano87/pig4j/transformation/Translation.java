package io.github.steromano87.pig4j.transformation;

import io.github.steromano87.pig4j.Anchor;
import io.github.steromano87.pig4j.options.PositionOptions;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Slf4j
@EqualsAndHashCode
public class Translation implements Transformation {
    private final int toX;
    private final int toY;
    private final Anchor anchor;
    private boolean autoResizeCanvas = true;

    private Integer overriddenCanvasWidth;
    private Integer overriddenCanvasHeight;

    public Translation(int toX, int toY) {
        this(toX, toY, Anchor.TOP_LEFT);
    }

    public Translation(int toX, int toY, Anchor anchor) {
        this.toX = toX;
        this.toY = toY;
        this.anchor = anchor;
    }

    /**
     * Specifies if the canvas should automatically resize to fit the new image position.
     * <p>
     * If the autoresize option is off, part of the element may exceed image limits
     * and will be trimmed from the rendered image.
     * <p>
     * If the autoresize option is on, the rendered image size will be adapted to fit the exceeding part of the image.
     * This option will have no effect if the superimposed element already fits the existing canvas.
     *
     * @param autoResizeCanvas if the autoresize option is turned on
     */
    public void setAutoResizeCanvas(boolean autoResizeCanvas) {
        this.autoResizeCanvas = autoResizeCanvas;
    }

    /**
     * Manually sets the final image canvas size.
     * <p>
     * Using this method will automatically turn the autoresize option to off.
     *
     * @param overriddenCanvasWidth  the overridden canvas width
     * @param overriddenCanvasHeight the overridden canvas height
     * @see PositionOptions#setAutoResizeCanvas
     */
    public void overrideCanvasSize(int overriddenCanvasWidth, int overriddenCanvasHeight) {
        this.overriddenCanvasWidth = overriddenCanvasWidth;
        this.overriddenCanvasHeight = overriddenCanvasHeight;
        this.setAutoResizeCanvas(false);
    }

    /**
     * Translates the given image.
     *
     * @param input the image to be translated
     * @return the translated image, rendered on a transparent background
     */
    @Override
    public BufferedImage transform(BufferedImage input) {
        if (this.isNoOp(input)) {
            return input;
        }

        // Initialize a canvas with size equal to the calculated final image
        int outputImageWidth = this.calculateFinalImageWidth(input.getWidth());
        int outputImageHeight = this.calculateFinalImageHeight(input.getHeight());

        BufferedImage outputImage = new BufferedImage(outputImageWidth, outputImageHeight, BufferedImage.TYPE_INT_ARGB);

        // Calculate start drawing point for the foreground image
        int startDrawingPointX = (int) Math.round(this.toX - (input.getWidth() * this.anchor.getHorizontalRelativePosition()));
        int startDrawingPointY = (int) Math.round(this.toY - (input.getHeight() * this.anchor.getVerticalRelativePosition()));

        // Draw the image on the canvas using the assigned position
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(input, startDrawingPointX, startDrawingPointY, null);
        graphics2D.dispose();

        return outputImage;
    }

    private int calculateFinalImageWidth(int originalWidth) {
        if (Objects.nonNull(this.overriddenCanvasWidth)) {
            return this.overriddenCanvasWidth;
        }

        if (!this.autoResizeCanvas) {
            return originalWidth;
        }

        int newImageMinX = (int) Math.round(this.toX - (originalWidth * this.anchor.getHorizontalRelativePosition()));
        int newImageMaxX = newImageMinX + originalWidth;

        int minX = Math.min(0, newImageMinX);
        int maxX = Math.max(originalWidth, newImageMaxX);

        return maxX - minX;
    }

    private int calculateFinalImageHeight(int originalHeight) {
        if (Objects.nonNull(this.overriddenCanvasHeight)) {
            return this.overriddenCanvasHeight;
        }

        if (!this.autoResizeCanvas) {
            return originalHeight;
        }

        int newImageMinY = (int) Math.round(this.toY - (originalHeight * this.anchor.getVerticalRelativePosition()));
        int newImageMaxY = newImageMinY + originalHeight;

        int minY = Math.min(0, newImageMinY);
        int maxY = Math.max(originalHeight, newImageMaxY);

        return maxY - minY;
    }

    private boolean isNoOp(BufferedImage input) {
        return (this.toX == Math.floor(input.getWidth() * this.anchor.getHorizontalRelativePosition()) &&
                this.toY == Math.floor(input.getHeight() * this.anchor.getVerticalRelativePosition())) &&
                (Objects.isNull(this.overriddenCanvasWidth) && Objects.isNull(this.overriddenCanvasHeight));
    }

}
