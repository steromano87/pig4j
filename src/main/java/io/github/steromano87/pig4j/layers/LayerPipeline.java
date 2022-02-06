package io.github.steromano87.pig4j.layers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LayerPipeline {
    private final List<Entry> entries = new ArrayList<>();
    private boolean forceCacheInvalidation = false;

    public void addLayer(Layer layer) {
        Entry layerEntry = new Entry(layer);
        this.entries.add(layerEntry);
    }

    public void setForceCacheInvalidation(boolean cacheInvalidation) {
        this.forceCacheInvalidation = cacheInvalidation;
    }

    public BufferedImage apply(BufferedImage initialImage) {
        boolean localCacheInvalidation = false;
        BufferedImage intermediateImage = initialImage;

        for (Entry entry : this.entries) {
            if (entry.isCacheInvalid() || this.forceCacheInvalidation) {
                localCacheInvalidation = true;
            }

            if (localCacheInvalidation) {
                intermediateImage = entry.apply(intermediateImage);
            } else {
                intermediateImage = entry.getCachedImage();
            }
        }

        return intermediateImage;
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public static class Entry {
        private final Layer layer;
        private int cachedHash;
        private BufferedImage cachedImage;

        public Entry(Layer layer) {
            this.layer = layer;
            this.cachedHash = layer.hashCode();
        }

        public BufferedImage apply(BufferedImage inputImage) {
            this.cachedHash = this.layer.hashCode();
            this.cachedImage = this.layer.apply(inputImage);
            return this.cachedImage;
        }

        public BufferedImage getCachedImage() {
            return this.cachedImage;
        }

        public boolean isCacheInvalid() {
            return Objects.isNull(this.cachedImage) || this.layer.hashCode() != this.cachedHash;
        }
    }
}
