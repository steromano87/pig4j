package io.github.steromano87.pig4j;

import java.awt.image.BufferedImage;

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

    public int translateToTopLeftX(BufferedImage image, int x) {
        return x - Math.toIntExact(Math.round(image.getWidth() * this.horizRelativePosition));
    }

    public int translateToTopLeftY(BufferedImage image, int y) {
        return y - Math.toIntExact(Math.round(image.getHeight() * this.vertRelativePosition));
    }
}
