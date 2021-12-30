package io.github.steromano87.pig4j.layers.transform;

import com.twelvemonkeys.image.ResampleOp;
import io.github.steromano87.pig4j.layers.Layer;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.Objects;

public class ScalingLayer implements Layer {
    private Integer width;
    private Integer height;
    private Double scaleX = 1.0;
    private Double scaleY = 1.0;

    private boolean uniformScaling = true;
    private Algorithm algorithm = Algorithm.LANCZOS;

    public ScalingLayer setWidth(int width) {
        if (this.uniformScaling) {
            this.height = null;
        }
        this.width = width;
        this.blankScales();

        return this;
    }

    public ScalingLayer setHeight(int height) {
        if (this.uniformScaling) {
            this.width = null;
        }
        this.height = height;
        this.blankScales();

        return this;
    }

    public ScalingLayer setScaleX(double scale) {
        if (this.uniformScaling) {
            throw new IllegalStateException("Cannot set single-axis scale if the uniform scaling option is active");
        }
        this.scaleX = scale;

        // Do not leave the other scale unset
        if (Objects.isNull(this.scaleY)) {
            this.scaleY = scale;
        }
        this.blankSizes();
        return this;
    }

    public ScalingLayer setScaleY(double scale) {
        if (this.uniformScaling) {
            throw new IllegalStateException("Cannot set single-axis scale if the uniform scaling option is active");
        }
        this.scaleY = scale;

        // Do not leave the other scale unset
        if (Objects.isNull(this.scaleX)) {
            this.scaleX = scale;
        }
        this.blankSizes();
        return this;
    }

    public ScalingLayer setScale(double scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.blankSizes();
        return this;
    }

    public ScalingLayer setUniformScaling(boolean uniformScaling) {
        this.uniformScaling = uniformScaling;
        return this;
    }

    public ScalingLayer setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        BufferedImageOp resampleOp = new ResampleOp(
                this.calculateFinalWidth(originalWidth, originalHeight),
                this.calculateFinalHeight(originalWidth, originalHeight),
                this.algorithm.getFilter()
        );
        return resampleOp.filter(image, null);
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
            return Math.toIntExact(Math.round((double) this.width * this.scaleX));
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
            return Math.toIntExact(Math.round((double) this.height * this.scaleY));
        }

        throw new IllegalStateException("This layer is not operating neither on sizes nor on scales");
    }

    private boolean isOperatingOnSizes() {
        return (Objects.isNull(this.width) || Objects.isNull(this.height)) &&
                (Objects.nonNull(this.scaleX) && Objects.nonNull(this.scaleY));
    }

    private boolean isOperatingOnScales() {
        return (Objects.nonNull(this.width) || Objects.nonNull(this.height)) &&
                (Objects.isNull(this.scaleX) && Objects.isNull(this.scaleY));
    }

    public enum Algorithm {
        CUBIC(ResampleOp.FILTER_QUADRATIC),
        LANCZOS(ResampleOp.FILTER_LANCZOS),
        LINEAR(ResampleOp.FILTER_TRIANGLE),
        NEAREST(ResampleOp.FILTER_POINT);

        private final int filter;

        Algorithm(int filter) {
            this.filter = filter;
        }

        public int getFilter() {
            return this.filter;
        }
    }
}
