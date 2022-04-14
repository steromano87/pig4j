package io.github.steromano87.pig4j.options;

import lombok.EqualsAndHashCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that holds the scaling options used when merging two images together.
 * <p>
 * The option defines how an image with an arbitrary size is fitted on an existing canvas.
 */
@EqualsAndHashCode
public class ScalingOptions {
    private Integer width;
    private Integer height;
    private Double scaleX = 1.0;
    private Double scaleY = 1.0;

    private boolean uniformScaling = true;
    private Algorithm algorithm = Algorithm.AUTO;

    /**
     * Sets the final width of the image.
     * <p>
     * If the uniform scaling is enabled, the height of the image will be adapted to preserve the aspect ratio.
     *
     * @param width the target width of the image
     */
    public void setWidth(int width) {
        if (this.uniformScaling) {
            this.height = null;
        }
        this.width = width;
        this.blankScales();
    }

    /**
     * Sets the final height of the image.
     * <p>
     * If the uniform scaling is enabled, the width of the image will be adapted to preserve the aspect ratio.
     *
     * @param height the target height of the image
     */
    public void setHeight(int height) {
        if (this.uniformScaling) {
            this.width = null;
        }
        this.height = height;
        this.blankScales();
    }

    /**
     * Sets the horizontal scaling factor of the image.
     * <p>
     * This method cannot be used if the uniform scaling is enabled (use {@link ScalingOptions#setScale instead}
     *
     * @param scale the target horizontal scale of the image
     * @throws IllegalStateException if the method is called when the uniform scaling option is on
     */
    public void setScaleX(double scale) {
        if (this.uniformScaling) {
            throw new IllegalStateException("Cannot set single-axis scale if the uniform scaling option is active");
        }
        this.scaleX = scale;

        // Do not leave the other scale unset
        if (Objects.isNull(this.scaleY)) {
            this.scaleY = scale;
        }
        this.blankSizes();
    }

    /**
     * Sets the vertical scaling factor of the image.
     * <p>
     * This method cannot be used if the uniform scaling is enabled (use {@link ScalingOptions#setScale instead}
     *
     * @param scale the target vertical scale of the image
     * @throws IllegalStateException if the method is called when the uniform scaling option is on
     */
    public void setScaleY(double scale) {
        if (this.uniformScaling) {
            throw new IllegalStateException("Cannot set single-axis scale if the uniform scaling option is active");
        }
        this.scaleY = scale;

        // Do not leave the other scale unset
        if (Objects.isNull(this.scaleX)) {
            this.scaleX = scale;
        }
        this.blankSizes();
    }

    /**
     * Sets the horizontal and vertical scaling of the image.
     *
     * @param scale the target scale of the image.
     */
    public void setScale(double scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.blankSizes();
    }

    /**
     * Specifies if the aspect ratio of the image should be preserved during scaling.
     * <p>
     * If this option is turned on, the image can be shrunk or enlarged only along one axis.
     *
     * @param uniformScaling whether the aspect ratio should be preserved or not during the image scaling
     */
    public void setUniformScaling(boolean uniformScaling) {
        this.uniformScaling = uniformScaling;
    }

    /**
     * Sets the scaling algorithm to use.
     *
     * @param algorithm the scaling algorithm
     * @see Algorithm
     */
    public void setScalingAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Scales the image.
     *
     * @param image the input image
     * @return the scaled image
     */
    public BufferedImage scale(BufferedImage image) {
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

    private void blankScales() {
        this.scaleX = null;
        this.scaleY = null;
    }

    private void blankSizes() {
        this.width = null;
        this.height = null;
    }

    private int calculateFinalWidth(int originalWidth, int originalHeight) {
        if (this.isOperatingOnSizes()) {
            if (Objects.nonNull(this.width)) {
                return this.width;
            } else {
                return Math.toIntExact(
                        Math.round(((double) this.height / originalHeight) * originalWidth)
                );
            }
        }

        if (this.isOperatingOnScales()) {
            return Math.toIntExact(Math.round((double) originalWidth * this.scaleX));
        }

        throw new IllegalStateException("This layer is not operating neither on sizes nor on scales");
    }

    private int calculateFinalHeight(int originalWidth, int originalHeight) {
        if (this.isOperatingOnSizes()) {
            if (Objects.nonNull(this.height)) {
                return this.height;
            } else {
                return Math.toIntExact(
                        Math.round(((double) this.width / originalWidth) * originalHeight)
                );
            }
        }

        if (this.isOperatingOnScales()) {
            return Math.toIntExact(Math.round((double) originalHeight * this.scaleY));
        }

        throw new IllegalStateException("This layer is not operating neither on sizes nor on scales");
    }

    private boolean isNoOp(BufferedImage input) {
        if (this.isOperatingOnSizes()) {
            return (input.getWidth() == this.width && input.getHeight() == this.height);
        }

        if (this.isOperatingOnScales()) {
            return (this.scaleX == 1.0 && this.scaleY == 1.0);
        }

        return false;
    }

    private boolean isOperatingOnScales() {
        return (Objects.isNull(this.width) || Objects.isNull(this.height)) &&
                (Objects.nonNull(this.scaleX) && Objects.nonNull(this.scaleY));
    }

    private boolean isOperatingOnSizes() {
        return (Objects.nonNull(this.width) || Objects.nonNull(this.height)) &&
                (Objects.isNull(this.scaleX) && Objects.isNull(this.scaleY));
    }

    private RenderingHints calculateScalingRenderingHints(BufferedImage image) {
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

    private boolean isEnlargement(BufferedImage image) {
        if (this.isOperatingOnScales()) {
            return this.scaleX > 1.0 || this.scaleY > 1.0;
        } else {
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            return this.calculateFinalWidth(originalWidth, originalHeight) > originalWidth ||
                    this.calculateFinalHeight(originalWidth, originalHeight) > originalHeight;
        }
    }

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
