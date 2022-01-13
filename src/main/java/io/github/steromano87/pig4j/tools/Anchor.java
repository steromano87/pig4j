package io.github.steromano87.pig4j.tools;

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
