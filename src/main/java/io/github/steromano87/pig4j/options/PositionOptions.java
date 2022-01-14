package io.github.steromano87.pig4j.options;

import java.awt.image.BufferedImage;

import io.github.steromano87.pig4j.tools.Anchor;
import io.github.steromano87.pig4j.tools.Translator;

@Deprecated
public class PositionOptions {
    private final Translator translator = new Translator();

    public PositionOptions setX(int x) {
        this.translator.setX(x);
        return this;
    }

    public PositionOptions setY(int y) {
        this.translator.setY(y);
        return this;
    }

    public PositionOptions setAnchor(Anchor anchor) {
        this.translator.setAnchor(anchor);
        return this;
    }

    public PositionOptions setAutoResizeCanvas(boolean autoResizeCanvas) {
        this.translator.setAutoResizeCanvas(autoResizeCanvas);
        return this;
    }

    public BufferedImage apply(BufferedImage bgImage, BufferedImage fgImage) {
        this.translator.overrideCanvasSize(bgImage.getWidth(), bgImage.getHeight());
        return this.translator.translate(fgImage);
    }
}
