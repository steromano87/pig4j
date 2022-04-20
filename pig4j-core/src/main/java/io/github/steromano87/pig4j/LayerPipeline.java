package io.github.steromano87.pig4j;

import io.github.steromano87.pig4j.cache.CacheManager;
import io.github.steromano87.pig4j.cache.Cacheable;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LayerPipeline implements Layer {
    @EqualsAndHashCode.Include
    private final List<Layer> layers = new ArrayList<>();

    @EqualsAndHashCode.Include
    private boolean forceCacheInvalidation = false;

    private final CacheManager cacheManager = new CacheManager();

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public void setForceCacheInvalidation(boolean cacheInvalidation) {
        this.forceCacheInvalidation = cacheInvalidation;
    }

    @Override
    public BufferedImage apply(BufferedImage initialImage) {
        if (!this.forceCacheInvalidation && this.cacheManager.isCachedImageValid(this.hashCode(), initialImage)) {
            log.debug("Cache is still valid, using cached image");
            return this.cacheManager.getCachedImage();
        }

        log.debug("Layer pipeline processing start (pipeline size: {})", this.layers.size());
        BufferedImage intermediateImage = initialImage;

        for (Layer layer : this.layers) {
            log.debug("Working on layer [{}]", layer);

            if (!this.forceCacheInvalidation &&
                    Cacheable.class.isAssignableFrom(layer.getClass()) &&
                    ((Cacheable) layer).isCachedImageValid(intermediateImage)) {
                log.debug("Entry cache is valid, using cached image");
                intermediateImage = ((Cacheable) layer).getCachedImage();
            } else {
                log.debug("Applying layer");
                intermediateImage = layer.apply(intermediateImage);
            }
        }

        log.debug("Layer pipeline execution ended");
        this.cacheManager.refreshInputImageHash(initialImage);
        this.cacheManager.refreshConfigurationHash(this.hashCode());
        this.cacheManager.setCachedImage(intermediateImage);

        return this.cacheManager.getCachedImage();
    }

    public BufferedImage getGeneratedImage() {
        return this.cacheManager.getCachedImage();
    }

    public boolean isEmpty() {
        return this.layers.isEmpty();
    }
}
