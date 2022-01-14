package io.github.steromano87.pig4j.tools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Translator {
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
                this.y == Math.floor(input.getHeight() * this.anchor.getVerticalRelativePosition())) ||
                (Objects.nonNull(this.overriddenCanvasWidth) && Objects.nonNull(this.overriddenCanvasHeight));
    }
}
