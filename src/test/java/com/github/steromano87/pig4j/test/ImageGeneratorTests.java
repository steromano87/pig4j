package com.github.steromano87.pig4j.test;

import com.github.steromano87.pig4j.ImageGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class ImageGeneratorTests {
    @Test
    void testSimpleInstantiation() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void testSimpleInstantiationWithBackgroundColor() {
        ImageGenerator generator = new ImageGenerator(640, 480, Color.BLACK);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void testSimpleInstantiationWithAlphaChannel() {
        ImageGenerator generator = new ImageGenerator(640, 480, null,true);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }
}
