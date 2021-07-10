package com.github.steromano87.pig4j.layers.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.github.steromano87.pig4j.layers.Layer;
import com.github.steromano87.pig4j.options.BlendingOptions;
import com.github.steromano87.pig4j.serialization.ColorDeserializer;
import com.github.steromano87.pig4j.serialization.ColorSerializer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(as = Layer.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TextLayer implements Layer {
    @JacksonXmlElementWrapper(localName = "textBlocks")
    @JacksonXmlProperty(localName = "textBlock")
    @JsonProperty
    private List<TextBlock> textBlocks = new ArrayList<>();

    @JsonProperty
    private BlendingOptions blendingOptions = new BlendingOptions();

    public TextLayer setTextBlocks(List<TextBlock> textBlocks) {
        this.textBlocks = textBlocks;
        return this;
    }

    public TextLayer addTextBlock(TextBlock textBlock) {
        this.textBlocks.add(textBlock);
        return this;
    }

    public TextLayer setBlendingOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        // Image should support transparency if even one text block has an alpha color
        boolean hasTransparency = this.textBlocks.stream().anyMatch(tb -> tb.getColor().getAlpha() < 255);
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage textImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

        for (TextBlock textBlock : this.textBlocks) {
            this.drawTextBlock(textBlock, textImage);
        }
        return this.blendingOptions.apply(image, textImage);
    }

    private void drawTextBlock(TextBlock textBlock, BufferedImage canvas) {
        Graphics2D graphics2D = canvas.createGraphics();
        graphics2D.setColor(textBlock.getColor());
        Font font = new Font(
                textBlock.getFont(),
                Font.PLAIN,
                textBlock.getSize()
        );
        graphics2D.setFont(font);
        graphics2D.drawString(textBlock.getContent(), textBlock.getX(), textBlock.getY());
        graphics2D.dispose();
    }

    @JacksonXmlRootElement(localName = "textBlock")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public static class TextBlock {
        @JacksonXmlText
        private String content;

        @JacksonXmlProperty(isAttribute = true)
        private String font = "Serif";

        @JacksonXmlProperty(isAttribute = true)
        private Integer size = 12;

        @JacksonXmlProperty(isAttribute = true)
        @JsonSerialize(using = ColorSerializer.class)
        @JsonDeserialize(using = ColorDeserializer.class)
        private Color color = Color.BLACK;

        @JacksonXmlProperty(isAttribute = true)
        private int x = 0;

        @JacksonXmlProperty(isAttribute = true)
        private int y = 0;

        public TextBlock setContent(String content) {
            this.content = content;
            return this;
        }

        public TextBlock setFont(String font) {
            this.font = font;
            return this;
        }

        public TextBlock setSize(Integer size) {
            this.size = size;
            return this;
        }

        public TextBlock setColor(Color color) {
            this.color = color;
            return this;
        }

        public TextBlock setX(int x) {
            this.x = x;
            return this;
        }

        public TextBlock setY(int y) {
            this.y = y;
            return this;
        }

        public String getContent() {
            return content;
        }

        public String getFont() {
            return font;
        }

        public Integer getSize() {
            return size;
        }

        public Color getColor() {
            return color;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
