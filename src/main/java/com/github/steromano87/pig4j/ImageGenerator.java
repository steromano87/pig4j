package com.github.steromano87.pig4j;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.github.steromano87.pig4j.layers.Layer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

@JsonRootName("configuration")
@JacksonXmlRootElement(localName = "configuration")
public class ImageGenerator {
    @JacksonXmlElementWrapper(localName = "layers")
    @JacksonXmlProperty(localName = "layer")
    @JsonProperty
    private final ArrayList<Layer> layers = new ArrayList<>();

    @JsonIgnore
    private BufferedImage processedImage;

    public ImageGenerator(int width, int height) {
        this(width, height, Color.WHITE);
    }

    public ImageGenerator(int width, int height, Color backgroundColor) {
        this(width, height, false);
        this.fillWithBackgroundColor(backgroundColor);
    }

    public ImageGenerator(int width, int height, boolean hasAlphaChannel) {
        int imageType = hasAlphaChannel ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        this.processedImage = new BufferedImage(width, height, imageType);
    }

    public ImageGenerator addLayer(Layer layer) {
        this.layers.add(layer);
        return this;
    }

    public static ImageGenerator fromXmlConfig(File xmlFile) throws IOException {
        ObjectMapper mapper = ImageGenerator.configureMapperUsingProperty(new XmlMapper());
        return mapper.readValue(xmlFile, ImageGenerator.class);
    }

    public static ImageGenerator fromJsonConfig(File jsonFile) throws IOException {
        ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper());
        return mapper.readValue(jsonFile, ImageGenerator.class);
    }

    public static ImageGenerator fromYamlConfig(File yamlFile) throws IOException {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        yamlFactory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
        ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper(yamlFactory));
        return mapper.readValue(yamlFile, ImageGenerator.class);
    }

    public void toXmlConfig(File xmlFile) throws IOException {
        XmlMapper mapper = (XmlMapper) ImageGenerator.configureMapperUsingProperty(new XmlMapper());
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        mapper.writeValue(xmlFile, this);
    }

    public void toJsonConfig(File jsonFile) throws IOException {
        ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper());
        mapper.writeValue(jsonFile, this);
    }

    public void toYamlConfig(File yamlFile) throws IOException {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        yamlFactory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
        ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper(yamlFactory));
        mapper.writeValue(yamlFile, this);
    }

    public ImageGenerator build() {
        if (this.layers.isEmpty()) {
            throw new IllegalStateException("No layer has been added");
        }

        for (Layer layer : this.layers) {
            this.processedImage = layer.apply(this.processedImage);
        }
        return this;
    }

    public BufferedImage toImage() {
        return this.processedImage;
    }

    public byte[] toByteArray(ImageFormat format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(this.processedImage, format.getExtension(), outputStream);
        return outputStream.toByteArray();
    }

    public boolean toFile(File file, ImageFormat format) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        return ImageIO.write(this.processedImage, format.getExtension(), outputStream);
    }

    public String toBase64(ImageFormat format) throws IOException {
        byte[] byteArray = this.toByteArray(format);
        return Base64.getEncoder().encodeToString(byteArray);
    }

    public String toDataUrl(ImageFormat format) throws IOException {
        String base64image = this.toBase64(format);
        String mimeType = format.getMimeType();
        return String.format("data:%s;base64,%s", mimeType, base64image);
    }

    private void fillWithBackgroundColor(Color color) {
        Graphics2D graphics2D = this.processedImage.createGraphics();
        graphics2D.setBackground(color);
        graphics2D.clearRect(0, 0, this.processedImage.getWidth(), this.processedImage.getHeight());
        graphics2D.dispose();
    }

    private static ObjectMapper configureMapperUsingProperty(ObjectMapper mapper) {
        TypeResolverBuilder<?> typeResolverBuilder = ObjectMapper.DefaultTypeResolverBuilder.construct(
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                mapper.getPolymorphicTypeValidator()
        );
        typeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        typeResolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        typeResolverBuilder.typeProperty("class");
        mapper.setDefaultTyping(typeResolverBuilder);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        return mapper;
    }

    private static ObjectMapper configureMapperUsingWrapperObject(ObjectMapper mapper) {
        TypeResolverBuilder<?> typeResolverBuilder = ObjectMapper.DefaultTypeResolverBuilder.construct(
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                mapper.getPolymorphicTypeValidator()
        );
        typeResolverBuilder.init(JsonTypeInfo.Id.NAME, null);
        typeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        mapper.setDefaultTyping(typeResolverBuilder);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return mapper;
    }
}
