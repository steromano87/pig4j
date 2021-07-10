package com.github.steromano87.pig4j.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorDeserializer extends StdDeserializer<Color> {
    public ColorDeserializer() {
        this(null);
    }

    public ColorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode colorNode = p.getCodec().readTree(p);
        return this.buildFromRGBString(colorNode.asText());
    }

    private Color buildFromRGBString(String v) throws IOException {
        Pattern pattern = Pattern.compile(
                "^(rgba?)\\((\\d+(?:\\.\\d+)?),\\s*(\\d+(?:\\.\\d+)?),\\s*(\\d+(?:\\.\\d+)?)(?:,\\s*(\\d+(?:\\.\\d+)?))?\\)$");
        Matcher matcher = pattern.matcher(v);
        if (matcher.find()) {
            String colorType = matcher.group(1);
            float rComponent = this.normalize(Float.parseFloat(matcher.group(2)));
            float gComponent = this.normalize(Float.parseFloat(matcher.group(3)));
            float bComponent = this.normalize(Float.parseFloat(matcher.group(4)));
            float alpha;
            try {
                alpha = this.normalize(Float.parseFloat(matcher.group(5)));
            } catch (IndexOutOfBoundsException exc) {
                alpha = 1.0f;
            }

            if (colorType.equalsIgnoreCase("rgb") && alpha < 1) {
                throw new IllegalStateException("An RGB color cannot have an alpha channel less than 1.0 (255)");
            }

            return new Color(rComponent, gComponent, bComponent, alpha);
        } else {
            throw new IOException("Invalid RGB(A) sequence provided");
        }
    }

    private float normalize(float component) {
        if (component <= 1) {
            return component;
        } else {
            return component / 255;
        }
    }
}
