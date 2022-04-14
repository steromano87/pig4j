package io.github.steromano87.pig4j.layers.base;

import io.github.steromano87.pig4j.Layer;
import io.github.steromano87.pig4j.options.BlendingOptions;
import io.github.steromano87.pig4j.options.PositionOptions;
import io.github.steromano87.pig4j.options.RotationOptions;
import io.github.steromano87.pig4j.options.ScalingOptions;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

@EqualsAndHashCode
public class TextLayer implements Layer {
    private String text = "";
    private String fontName = "Serif";
    private Integer fontSize = 12;
    private Integer fontType = Font.PLAIN;
    private Color color = Color.BLACK;

    private ScalingOptions scalingOptions = new ScalingOptions();
    private RotationOptions rotationOptions = new RotationOptions();
    private PositionOptions positionOptions = new PositionOptions();
    private BlendingOptions blendingOptions = new BlendingOptions();

    public TextLayer setText(String text) {
        this.text = text;
        return this;
    }

    public TextLayer setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public TextLayer setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextLayer setFontType(Integer fontType) {
        this.fontType = fontType;
        return this;
    }

    public TextLayer setColor(Color color) {
        this.color = color;
        return this;
    }

    public String getText() {
        return text;
    }

    public String getFontName() {
        return fontName;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Integer getFontType() {
        return fontType;
    }

    public Color getColor() {
        return color;
    }

    public ScalingOptions getScalingOptions() {
        return scalingOptions;
    }

    public RotationOptions getRotationOptions() {
        return rotationOptions;
    }

    public PositionOptions getPositionOptions() {
        return positionOptions;
    }

    public BlendingOptions getBlendingOptions() {
        return blendingOptions;
    }

    public TextLayer setScalingOptions(ScalingOptions scalingOptions) {
        this.scalingOptions = scalingOptions;
        return this;
    }

    public TextLayer setRotationOptions(RotationOptions rotationOptions) {
        this.rotationOptions = rotationOptions;
        return this;
    }

    public TextLayer setPositionOptions(PositionOptions positionOptions) {
        this.positionOptions = positionOptions;
        return this;
    }

    public TextLayer setBlendingOptions(BlendingOptions blendingOptions) {
        this.blendingOptions = blendingOptions;
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        boolean hasTransparency = this.getColor().getAlpha() < 255;
        int imageType = hasTransparency ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        // Calculate the text box size
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        Font font = new Font(this.fontName, this.fontType, this.fontSize);
        int textWidth = (int) Math.round(font.getStringBounds(this.text, frc).getWidth());
        int textHeight = (int) Math.round(font.getStringBounds(this.text, frc).getHeight());

        // Create empty canvas based on the calculated rendered text box
        BufferedImage textImage = new BufferedImage(textWidth, textHeight, imageType);
        Graphics2D graphics2D = textImage.createGraphics();
        graphics2D.setColor(this.getColor());
        graphics2D.setFont(font);
        graphics2D.drawString(this.getText(), 0, 0);
        graphics2D.dispose();

        // Apply the options to blend the text with the background image
        return this.applyOptionsStack(
                textImage,
                image,
                this.scalingOptions,
                this.rotationOptions,
                this.positionOptions,
                this.blendingOptions
        );
    }
}
