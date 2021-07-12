package com.github.steromano87.pig4j.options;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PositionOptions {
    private Hook imageHook = Hook.TOP_LEFT;

    private int x = 0;

    private int y = 0;

    public PositionOptions setX(int x) {
        this.x = x;
        return this;
    }

    public PositionOptions setY(int y) {
        this.y = y;
        return this;
    }

    public PositionOptions setImageHook(Hook imageHook) {
        this.imageHook = imageHook;
        return this;
    }

    public BufferedImage apply(BufferedImage bgImage, BufferedImage fgImage) {
        BufferedImage outputImage = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Draw the bgImage on the canvas using the assigned position
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(fgImage, 0, 0, null);
        graphics2D.dispose();

        return outputImage;
    }

    private int[] calculateStartDrawingPoint(int imageWidth, int imageHeight, int canvasWidth, int canvasHeight, Hook hook) {
        int startDrawingPointX = 0;
        int startDrawingPointY = 0;

        return new int[]{startDrawingPointX, startDrawingPointY};
    }

    public enum Hook {
        TOP_LEFT(0, 0),
        TOP_CENTER(0.5, 0),
        TOP_RIGHT(1, 0),
        MID_LEFT(0, 0.5),
        MID_CENTER(0.5, 0.5),
        MID_RIGHT(1, 0.5),
        BOTTOM_LEFT(0, 1),
        BOTTOM_CENTER(0.5, 1),
        BOTTOM_RIGHT(1, 1);

        double horizRelativePosition;

        double vertRelativePosition;

        Hook(double horizRelativePosition, double vertRelativePosition) {
            this.horizRelativePosition = horizRelativePosition;
            this.vertRelativePosition = vertRelativePosition;
        }

        double getHorizontalRelativePosition() {
            return this.horizRelativePosition;
        }

        double getVerticalRelativePosition() {
            return this.vertRelativePosition;
        }
    }
}
