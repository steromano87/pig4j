package io.github.steromano87.pig4j.cache;

import java.awt.image.BufferedImage;

public interface Cacheable {
    boolean isCachedImageValid(BufferedImage currentInputImage);

    BufferedImage getCachedImage();
}
