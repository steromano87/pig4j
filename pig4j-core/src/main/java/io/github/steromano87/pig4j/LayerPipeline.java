package io.github.steromano87.pig4j;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
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
        log.debug("Layer pipeline processing start (pipeline size: {})", this.entries.size());
        boolean localCacheInvalidation = false;
        BufferedImage intermediateImage = initialImage;

        for (Entry entry : this.entries) {
            log.debug("Working on entry [{}]", entry);
            if (entry.isCacheInvalid() || this.forceCacheInvalidation) {
                localCacheInvalidation = true;
            }

            if (localCacheInvalidation) {
                log.debug("Entry cache is marked as invalid, forcing layer application");
                intermediateImage = entry.apply(intermediateImage);
            } else {
                log.debug("Entry cache is valid, using cached image");
                intermediateImage = entry.getCachedImage();
            }
        }

        log.debug("Layer pipeline execution ended");
        return this.getGeneratedImage();
    }

    public BufferedImage getGeneratedImage() {
        return this.entries.get(this.entries.size() - 1).getCachedImage();
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LayerPipeline pipeline = (LayerPipeline) o;

        if (forceCacheInvalidation != pipeline.forceCacheInvalidation) return false;
        return entries.equals(pipeline.entries);
    }

    @Override
    public int hashCode() {
        return entries.stream()
                .map(Object::hashCode)
                .reduce(Integer::sum)
                .orElse(0);
    }

    @ToString
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return layer.equals(entry.layer);
        }

        @Override
        public int hashCode() {
            return layer.hashCode();
        }
    }
}
