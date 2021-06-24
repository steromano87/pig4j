package io.github.steromano87.pig4j.layers.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.github.steromano87.pig4j.exceptions.ImageReadingException;
import io.github.steromano87.pig4j.layers.Layer;
import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Stream;

@JsonDeserialize(as = Layer.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ImageLayer implements Layer {
    @JsonIgnore
    private BufferedImage sourceImage;

    @JacksonXmlProperty(isAttribute = true)
    @JsonProperty
    private File imageFile;

    @JacksonXmlProperty(isAttribute = true)
    @JsonProperty
    private URL imageUrl;

    @JacksonXmlCData
    @JsonProperty
    private String imageBase64;

    private ScalingOptions scalingOptions = new ScalingOptions();

    private BlendingOptions blendingOptions = new BlendingOptions();

    public ImageLayer setImageFile(File imageFile) {
        this.imageFile = imageFile;
        try {
            this.sourceImage = ImageIO.read(imageFile);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from file", exc);
        }

        return this;
    }

    public ImageLayer setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        try {
            this.sourceImage = ImageIO.read(imageUrl);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot read image from URL", exc);
        }

        return this;
    }

    public ImageLayer setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
        try {
            byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            this.sourceImage = ImageIO.read(inputStream);
        } catch (IOException exc) {
            throw new ImageReadingException("Cannot decode Base64 string as image", exc);
        }

        return this;
    }

    public ImageLayer setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        return this;
    }

    public ImageLayer setScalingOptions(ScalingOptions scalingOptions) {
        this.scalingOptions = scalingOptions;
        return this;
    }

    public ImageLayer setFusionOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        this.checkStateConsistency();
        return this.blendingOptions.apply(
                image,
                this.scalingOptions.apply(this.sourceImage)
        );
    }

    private void checkStateConsistency() {
        if (Stream.of(this.imageFile, this.imageUrl, this.imageBase64).allMatch(Objects::isNull)) {
            throw new IllegalStateException(
                    "One value between image file, image URL and image Base64 should not be null"
            );
        }

        if (Stream.of(this.imageFile, this.imageUrl, this.imageBase64).filter(Objects::nonNull).count() > 1) {
            throw new IllegalStateException(
                    "Only one value between image file, image URL and image Base64 can be specified"
            );
        }
    }
}
