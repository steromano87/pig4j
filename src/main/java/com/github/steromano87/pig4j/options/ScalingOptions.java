package com.github.steromano87.pig4j.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.twelvemonkeys.image.ResampleOp;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ScalingOptions {
    @JsonProperty
    @JacksonXmlProperty
    private Integer width;

    @JsonProperty
    @JacksonXmlProperty
    private Integer height;

    @JsonProperty
    @JacksonXmlProperty
    private Double scale;

    @JsonProperty
    @JacksonXmlProperty
    private Algorithm algorithm = Algorithm.LANCZOS;

    public ScalingOptions setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public ScalingOptions setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public ScalingOptions setScale(Double scale) {
        this.scale = scale;
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
                double scale = (double) this.height / originalHeight;
                this.width = Math.toIntExact(Math.round(originalWidth * scale));
            } else if (this.height == null) {
                double scale = (double) this.width / originalWidth;
                this.height = Math.toIntExact(Math.round(originalHeight * scale));
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
