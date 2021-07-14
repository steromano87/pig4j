package com.github.steromano87.pig4j.options;

import com.twelvemonkeys.image.ResampleOp;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Class that holds the scaling options used when merging two images together
 *
 * The option defines how an image with an arbitrary size is fitted on an existing canvas
 */
public class ScalingOptions {
    private Integer width;
    private Integer height;

    private Double scale = 1.0;

    private Algorithm algorithm = Algorithm.LANCZOS;

    public ScalingOptions setWidth(Integer width) {
        this.width = width;
        this.scale = null;
        return this;
    }

    public ScalingOptions setHeight(Integer height) {
        this.height = height;
        this.scale = null;
        return this;
    }

    public ScalingOptions setScale(Double scale) {
        this.scale = scale;
        this.width = null;
        this.height = null;
        return this;
    }

    public ScalingOptions setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public BufferedImage apply(BufferedImage image) {
        // No-op mode
        if (this.width == null && this.height == null && this.scale == null) {
            return image;
        }

        if (this.scale != null && (this.width != null || this.height != null)) {
            throw new IllegalStateException(
                    "Either the scale or (al least) one between width and height should be specified");
        }

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        if (this.scale == null) {
            if (this.width == null) {
                double calculatedScale = (double) this.height / originalHeight;
                this.width = Math.toIntExact(Math.round(originalWidth * calculatedScale));
            } else if (this.height == null) {
                double calculatedScale = (double) this.width / originalWidth;
                this.height = Math.toIntExact(Math.round(originalHeight * calculatedScale));
            }
        } else {
            this.width = Math.toIntExact(Math.round(originalWidth * this.scale));
            this.height = Math.toIntExact(Math.round(originalHeight * this.scale));
        }

        BufferedImageOp resampleOp = new ResampleOp(this.width, this.height, this.algorithm.getFilter());
        return resampleOp.filter(image, null);
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
