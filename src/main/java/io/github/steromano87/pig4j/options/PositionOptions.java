package io.github.steromano87.pig4j.options;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class PositionOptions {
    private int x = 0;
    private int y = 0;
    private Anchor anchor = Anchor.TOP_LEFT;
    private boolean autoResizeCanvas = true;

    private Integer overriddenCanvasWidth;
    private Integer overriddenCanvasHeight;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public void setAutoResizeCanvas(boolean autoResizeCanvas) {
        this.autoResizeCanvas = autoResizeCanvas;
    }

    public void overrideCanvasSize(int overriddenCanvasWidth, int overriddenCanvasHeight) {
        this.overriddenCanvasWidth = overriddenCanvasWidth;
        this.overriddenCanvasHeight = overriddenCanvasHeight;
        this.setAutoResizeCanvas(false);
    }

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

        public double getHorizontalRelativePosition() {
            return this.horizRelativePosition;
        }

        public double getVerticalRelativePosition() {
            return this.vertRelativePosition;
        }
    }
}
