package io.github.steromano87.pig4j.transform;

import java.awt.*;

public enum ScalingAlgorithm {
    AUTO(null),
    BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
    BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
    NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

    private final Object renderingHintValue;

    ScalingAlgorithm(Object renderingHintValue) {
        this.renderingHintValue = renderingHintValue;
    }

    public Object getRenderingHint() {
        return this.renderingHintValue;
    }
}
