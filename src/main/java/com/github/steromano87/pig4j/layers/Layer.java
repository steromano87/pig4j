package com.github.steromano87.pig4j.layers;

import java.awt.image.BufferedImage;

public interface Layer {
    BufferedImage apply(BufferedImage image);
}
