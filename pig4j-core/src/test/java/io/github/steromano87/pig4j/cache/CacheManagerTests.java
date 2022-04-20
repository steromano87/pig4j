package io.github.steromano87.pig4j.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class CacheManagerTests {
    private static final BufferedImage firstInputImage;
    private static final BufferedImage copyOfFirstInputImage;
    private static final BufferedImage secondInputImage;

    static {
        try {
            firstInputImage = ImageIO.read(
                    Objects.requireNonNull(
                            CacheManagerTests.class.getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                    )
            );

            copyOfFirstInputImage = ImageIO.read(
                    Objects.requireNonNull(
                            CacheManagerTests.class.getClassLoader().getResourceAsStream("common/landscape_640_400.jpg")
                    )
            );

            secondInputImage = ImageIO.read(
                    Objects.requireNonNull(
                            CacheManagerTests.class.getClassLoader().getResourceAsStream("common/trollface.png")
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCacheIsValidIfAllHashesAreTheSame() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.refreshInputImageHash(firstInputImage);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertTrue(cacheManager.isCachedImageValid(0, firstInputImage));
    }

    @Test
    void testCacheIsValidIfInputImageSourceIsTheSame() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.refreshInputImageHash(firstInputImage);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertTrue(cacheManager.isCachedImageValid(0, copyOfFirstInputImage));
    }

    @Test
    void testCacheIsInvalidIfConfigurationHashIsDifferent() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.refreshInputImageHash(firstInputImage);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertFalse(cacheManager.isCachedImageValid(1, firstInputImage));
    }

    @Test
    void testCacheIsInvalidIfInputImageIsDifferent() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.refreshInputImageHash(firstInputImage);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertFalse(cacheManager.isCachedImageValid(0, secondInputImage));
    }

    @Test
    void testCacheIsInvalidIfNoInputImageIsCached() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertFalse(cacheManager.isCachedImageValid(0, secondInputImage));
    }

    @Test
    void testCacheIsInvalidIfNoConfigurationIsCached() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshInputImageHash(firstInputImage);
        cacheManager.setCachedImage(firstInputImage);

        Assertions.assertFalse(cacheManager.isCachedImageValid(0, secondInputImage));
    }

    @Test
    void testCacheIsInvalidIfNoProcessedImageIsCached() {
        CacheManager cacheManager = new CacheManager();
        cacheManager.refreshConfigurationHash(0);
        cacheManager.refreshInputImageHash(firstInputImage);

        Assertions.assertFalse(cacheManager.isCachedImageValid(0, firstInputImage));
    }
}
