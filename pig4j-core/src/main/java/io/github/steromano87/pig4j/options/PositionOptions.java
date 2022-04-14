package io.github.steromano87.pig4j.options;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Class that holds options to translate an image in an existing canvas.
 */
public class PositionOptions {
    private int x = 0;
    private int y = 0;
    private Anchor anchor = Anchor.TOP_LEFT;
    private boolean autoResizeCanvas = true;

    private Integer overriddenCanvasWidth;
    private Integer overriddenCanvasHeight;

    /**
     * Sets the X position, according to the given {@link  Anchor}
     *
     * @param x the X position, in pixels
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the Y position, according to the given {@link Anchor}
     *
     * @param y the Y position, in pixels
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the anchor point against which the position of the image is calculated
     *
     * @param anchor the element anchor point
     */
    public void setAnchor(Anchor anchor) {
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
    public BufferedImage translate(BufferedImage input) {
        if (this.isNoOp(input)) {
            return input;
        }

        // Initialize a canvas with size equal to the calculated final image
        int outputImageWidth = this.calculateFinalImageWidth(input.getWidth());
        int outputImageHeight = this.calculateFinalImageHeight(input.getHeight());

        BufferedImage outputImage = new BufferedImage(outputImageWidth, outputImageHeight, BufferedImage.TYPE_INT_ARGB);

        // Calculate start drawing point for the foreground image
        int startDrawingPointX = (int) Math.round(this.x - (input.getWidth() * this.anchor.getHorizontalRelativePosition()));
        int startDrawingPointY = (int) Math.round(this.y - (input.getHeight() * this.anchor.getVerticalRelativePosition()));

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

        int newImageMinX = (int) Math.round(this.x - (originalWidth * this.anchor.getHorizontalRelativePosition()));
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

        int newImageMinY = (int) Math.round(this.y - (originalHeight * this.anchor.getVerticalRelativePosition()));
        int newImageMaxY = newImageMinY + originalHeight;

        int minY = Math.min(0, newImageMinY);
        int maxY = Math.max(originalHeight, newImageMaxY);

        return maxY - minY;
    }

    private boolean isNoOp(BufferedImage input) {
        return (this.x == Math.floor(input.getWidth() * this.anchor.getHorizontalRelativePosition()) &&
                this.y == Math.floor(input.getHeight() * this.anchor.getVerticalRelativePosition())) &&
                (Objects.isNull(this.overriddenCanvasWidth) && Objects.isNull(this.overriddenCanvasHeight));
    }

    /**
     * Image anchor point, against which the position is calculated.
     * <p>
     * The anchor will define the origin of the reference system to calculate the image translation.
     * By default, the origin is located in the top left corner of the image, but it can be changed if a more precise
     * location is required.
     * <p>
     * Anchor is expressed in terms of relative positions onto the target image.
     * Here, axes origin is located in the top left corner,
     * X axis points rightwards and Y axis points downwards.
     */
    public enum Anchor {
        TOP_LEFT(0, 0),
        TOP_CENTER(0.5, 0),
        TOP_RIGHT(1, 0),
        MID_LEFT(0, 0.5),
        MID_CENTER(0.5, 0.5),
        MID_RIGHT(1, 0.5),
        BOTTOM_LEFT(0, 1),
        BOTTOM_CENTER(0.5, 1),
        BOTTOM_RIGHT(1, 1);

        final double horizRelativePosition;

        final double vertRelativePosition;

        Anchor(double horizRelativePosition, double vertRelativePosition) {
            this.horizRelativePosition = horizRelativePosition;
            this.vertRelativePosition = vertRelativePosition;
        }

        /**
         * Returns the horizontal relative position.
         * <p>
         * The value spans from 0 to 1.
         *
         * @return the horizontal relative position
         */
        public double getHorizontalRelativePosition() {
            return this.horizRelativePosition;
        }

        /**
         * Returns the vertical relative position.
         * <p>
         * The value spans from 0 to 1.
         *
         * @return the vertical relative position
         */
        public double getVerticalRelativePosition() {
            return this.vertRelativePosition;
        }
    }
}
