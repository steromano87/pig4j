package io.github.steromano87.pig4j.options;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that holds the scaling options used when merging two images together
 *
 * The option defines how an image with an arbitrary size is fitted on an existing canvas
 */
public class ScalingOptions {
    private Integer width;
    private Integer height;
    private Double scaleX = 1.0;
    private Double scaleY = 1.0;

    private boolean uniformScaling = true;
    private Algorithm algorithm = Algorithm.AUTO;

    public void setWidth(int width) {
        if (this.uniformScaling) {
            this.height = null;
        }
        this.width = width;
        this.blankScales();
    }

    public void setHeight(int height) {
        if (this.uniformScaling) {
            this.width = null;
        }
        this.height = height;
        this.blankScales();
    }

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

    public void setScale(double scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.blankSizes();
    }

    public void setUniformScaling(boolean uniformScaling) {
        this.uniformScaling = uniformScaling;
    }

    public void setScalingAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

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

    public enum Algorithm {
        AUTO(null),
        BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
        BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
        NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        private final Object renderingHintValue;

        Algorithm(Object renderingHintValue) {
            this.renderingHintValue = renderingHintValue;
        }

        public Object getRenderingHint() {
            return this.renderingHintValue;
        }
    }
}
