package com.github.steromano87.pig4j.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.awt.*;
import java.io.IOException;

public class ColorSerializer extends StdSerializer<Color> {
    public ColorSerializer() {
        this(null);
    }

    public ColorSerializer(Class<Color> t) {
        super(t);
    }

    @Override
    public void serialize(Color value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(this.convertToRGBString(value));
    }

    private String convertToRGBString(Color v) {
        if (v.getAlpha() < 255) {
            return String.format("rgba(%d, %d, %d, %d)", v.getRed(), v.getGreen(), v.getBlue(), v.getAlpha());
        } else {
            return String.format("rgb(%d, %d, %d)", v.getRed(), v.getGreen(), v.getBlue());
        }
    }
}
