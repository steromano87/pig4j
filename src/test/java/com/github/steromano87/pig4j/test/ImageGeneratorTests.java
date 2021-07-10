package com.github.steromano87.pig4j.test;

import com.github.steromano87.pig4j.ImageGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

class ImageGeneratorTests {
    @Test
    void TestSimpleInstantiation() {
        ImageGenerator generator = new ImageGenerator(640, 480);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void TestSimpleInstantiationWithBackgroundColor() {
        ImageGenerator generator = new ImageGenerator(640, 480, Color.BLACK);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void TestSimpleInstantiationWithAlphaChannel() {
        ImageGenerator generator = new ImageGenerator(640, 480, true);
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }

    @Test
    void TestInstantiationFromXmlFile() {
        Path validFilePath = Paths.get("src/test/resources/imageGenerator/instantiation", "validInstantiation.xml");
        ImageGenerator generator = ImageGenerator.fromXmlConfig(validFilePath.toFile());
        Assertions.assertInstanceOf(ImageGenerator.class, generator);
    }
}
