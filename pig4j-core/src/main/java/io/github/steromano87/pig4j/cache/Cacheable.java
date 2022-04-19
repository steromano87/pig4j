package io.github.steromano87.pig4j.cache;

import java.awt.image.BufferedImage;

public interface Cacheable {
    default int getConfigurationHash() {
        return this.hashCode();
    }

    boolean isCacheInvalid(int newConfigurationHash, BufferedImage newInputImage);

    BufferedImage getCachedImage();
}
