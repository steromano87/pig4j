package com.github.steromano87.pig4j.options;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Class that holds options used to blend two images together
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BlendingOptions {
    @JsonProperty
    @JacksonXmlProperty
    private float alpha = 1.0f;

    @JsonProperty
    @JacksonXmlProperty
    private Method method = Method.NORMAL;

    public BlendingOptions setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public BlendingOptions setMethod(Method method) {
        this.method = method;
        return this;
    }

    /**
     * Blends two images together
     *
     * @param bgImage background image
     * @param fgImage foreground image
     * @return the blended image
     */
    public BufferedImage apply(BufferedImage bgImage, BufferedImage fgImage) {
        boolean hasTransparency = bgImage.getColorModel().hasAlpha();
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage outputImage = new BufferedImage(bgImage.getWidth(), bgImage.getHeight(), imageType);

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(bgImage, 0, 0, null);

        // TODO: set the various fusion options
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, this.alpha);
        graphics2D.setComposite(alphaComposite);
        graphics2D.drawImage(fgImage, 0, 0, null);
        graphics2D.dispose();

        return outputImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendingOptions that = (BlendingOptions) o;
        return Float.compare(that.alpha, alpha) == 0 &&
                method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, method);
    }

    /**
     * Blending method used while joining images
     *
     * WIP: currently only the NORMAL method is implicitly used
     */
    public enum Method {
        NORMAL,
        SUBTRACT
    }
}
