package io.github.steromano87.pig4j.transformation;

import io.github.steromano87.pig4j.options.ScalingOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

abstract class Scaling implements Transformation {
    protected boolean uniformScaling = true;
    private Algorithm algorithm = Algorithm.AUTO;

    /**
     * Sets the scaling algorithm to use.
     *
     * @param algorithm the scaling algorithm
     * @see ScalingOptions.Algorithm
     */
    public void setScalingAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    protected RenderingHints calculateScalingRenderingHints(BufferedImage image) {
        Map<RenderingHints.Key, Object> renderingHintsMap = new HashMap<>();
        Algorithm usedAlgorithm;

        // Set algorithm rendering hints
        if (Algorithm.AUTO.equals(this.algorithm)) {
            if (this.isEnlargement(image)) {
                usedAlgorithm = Algorithm.BICUBIC;
            } else {
                usedAlgorithm = Algorithm.BILINEAR;
            }
        } else {
            usedAlgorithm = this.algorithm;
        }

        renderingHintsMap.put(
                RenderingHints.KEY_INTERPOLATION,
                usedAlgorithm.getRenderingHint()
        );

        // Set default hints
        renderingHintsMap.put(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        renderingHintsMap.put(
                RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        );

        renderingHintsMap.put(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY
        );

        renderingHintsMap.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        return new RenderingHints(renderingHintsMap);
    }
    abstract protected boolean isEnlargement(BufferedImage image);

    /**
     * Scaling algorithm to be used while shrinking or enlarging the image.
     */
    public enum Algorithm {
        /**
         * Automatic selection of the algorithm:
         * <ul>
         *     <li>if the image is enlarged, the bicubic algorithm will be used</li>
         *     <li>if the image is shrunk, the bilinear algorithm will be used</li>
         * </ul>
         */
        AUTO(null),

        /**
         * Bicubic algorithm. More precise but slower.
         */
        BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),

        /**
         * Bilinear algorithm. Medium precision but faster than bicubic.
         */
        BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),

        /**
         * Nearest neighbor algorithm. The fastest, but the worse in terms of quality.
         */
        NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        private final Object renderingHintValue;

        Algorithm(Object renderingHintValue) {
            this.renderingHintValue = renderingHintValue;
        }

        /**
         * Returns the associated rendering hint.
         *
         * @return the corresponding rendering hint
         * @see RenderingHints
         */
        public Object getRenderingHint() {
            return this.renderingHintValue;
        }
    }
}
