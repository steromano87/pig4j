package io.github.steromano87.pig4j.transformation;

import java.awt.image.BufferedImage;

public interface Transformation {
    BufferedImage transform(BufferedImage initialImage);
}
