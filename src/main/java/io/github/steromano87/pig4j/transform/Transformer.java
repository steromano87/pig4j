package io.github.steromano87.pig4j.transform;

import java.awt.image.BufferedImage;

public interface Transformer {
    BufferedImage transform(BufferedImage input);
}
