package io.github.steromano87.pig4j.layers;

import java.awt.image.BufferedImage;

public interface Blender {
    BufferedImage blend(BufferedImage fgImage, BufferedImage bgImage);
}
