package io.github.steromano87.pig4j.options;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotationOptions {
    private double angle = 0.0;
    private boolean autoResizeCanvas = true;

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setAutoResizeCanvas(boolean autoResizeCanvas) {
        this.autoResizeCanvas = autoResizeCanvas;
    }

    public BufferedImage rotate(BufferedImage input) {
        if (this.isNoOp()) {
            return input;
        }

        // Initialize a canvas with size equal to the calculated final image
        int outputImageWidth = this.calculateFinalImageWidth(input.getWidth(), input.getHeight());
        int outputImageHeight = this.calculateFinalImageHeight(input.getWidth(), input.getHeight());

        BufferedImage outputImage = new BufferedImage(outputImageWidth, outputImageHeight, BufferedImage.TYPE_INT_ARGB);

        // Apply the rotation to the image
        Graphics2D graphics2D = outputImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.translate(
                (this.calculateFinalImageWidth(input.getWidth(), input.getHeight()) - input.getWidth()) * 0.5,
                (this.calculateFinalImageHeight(input.getWidth(), input.getHeight()) - input.getHeight()) * 0.5
        );
        transform.rotate(
                this.getAngleAsRadians(),
                input.getWidth() * 0.5,
                input.getHeight() * 0.5
        );
        graphics2D.setTransform(transform);
        graphics2D.drawImage(input, 0, 0, null);
        graphics2D.dispose();

        return outputImage;
    }

    private boolean isNoOp() {
        return this.angle % 360.0 == 0.0;
    }

    private int calculateFinalImageWidth(int originalWidth, int originalHeight) {
        if (!this.autoResizeCanvas) {
            return originalWidth;
        }

        return (int) Math.floor(
                originalWidth * Math.abs(Math.cos(this.getAngleAsRadians()))
                        + originalHeight * Math.abs(Math.sin(this.getAngleAsRadians()))
        );
    }

    private int calculateFinalImageHeight(int originalWidth, int originalHeight) {
        if (!this.autoResizeCanvas) {
            return originalHeight;
        }

        return (int) Math.floor(
                originalHeight * Math.abs(Math.cos(this.getAngleAsRadians()))
                        + originalWidth * Math.abs(Math.sin(this.getAngleAsRadians()))
        );
    }

    private Double getAngleAsRadians() {
        return Math.toRadians(this.angle);
    }
}
