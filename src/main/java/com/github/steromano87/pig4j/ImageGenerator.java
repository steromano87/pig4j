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
import com.github.steromano87.pig4j.exceptions.ConfigReadingException;
import com.github.steromano87.pig4j.exceptions.ConfigWritingException;
import com.github.steromano87.pig4j.exceptions.ImageGenerationException;
import com.github.steromano87.pig4j.exceptions.ImageWritingException;
import com.github.steromano87.pig4j.layers.Layer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

/**
 * Entry point class to generate a composite image.
 * <p>
 * This class accepts as input one or more {@link Layer} instances and generates a composite images by applying
 * all the layers in a FIFO order.
 *
 * @see Layer
 */
@JsonRootName("configuration")
@JacksonXmlRootElement(localName = "configuration")
public class ImageGenerator {
    @JacksonXmlElementWrapper(localName = "layers")
    @JacksonXmlProperty(localName = "layer")
    @JsonProperty
    private final ArrayList<Layer> layers = new ArrayList<>();

    @JsonIgnore
    private BufferedImage processedImage;

    @JacksonXmlProperty(localName = "width")
    @JsonProperty("width")
    private final int canvasWidth;

    @JacksonXmlProperty(localName = "height")
    @JsonProperty("height")
    private final int canvasHeight;

    @JacksonXmlProperty
    @JsonProperty
    private Color backgroundColor;

    @JacksonXmlProperty(localName = "alphaChannel")
    @JsonProperty("alphaChannel")
    private final boolean hasAlphaChannel;

    /**
     * Creates an empty image generator with white background and no alpha channel support
     *
     * @param width  the width of the image canvas, expressed in pixels
     * @param height the height of the image canvas, expressed in pixels
     */
    public ImageGenerator(@JsonProperty("width") int width, @JsonProperty("height") int height) {
        this(width, height, Color.WHITE);
    }

    /**
     * Creates an empty image generator, applying the give background color. Alpha channel support is disabled.
     *
     * @param width           the width of the image canvas, expressed in pixels
     * @param height          the height of the image canvas, expressed in pixels
     * @param backgroundColor the background color
     */
    public ImageGenerator(int width, int height, Color backgroundColor) {
        this(width, height, false);
        this.backgroundColor = backgroundColor;
        this.fillWithBackgroundColor(this.backgroundColor);
    }

    /**
     * Creates an empty image generator with alpha channel support. The background will be set as transparent.
     *
     * @param width           the width of the image canvas, expressed in pixels
     * @param height          he height of the image canvas, expressed in pixels
     * @param hasAlphaChannel whether the composed generator supports the alpha channel or not
     */
    public ImageGenerator(int width, int height, boolean hasAlphaChannel) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.hasAlphaChannel = hasAlphaChannel;
        int imageType = this.hasAlphaChannel ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        this.processedImage = new BufferedImage(this.canvasWidth, this.canvasHeight, imageType);
    }

    /**
     * Builds an image generator instance starting from an XML file.
     * <p>
     * XML format and specifications varies according to the used layer.
     *
     * @param xmlFile the file to use to build the image generator
     * @return the image generator instance, pre-configured according to the input file
     * @throws ConfigReadingException when the file is unreadable or malformed
     */
    public static ImageGenerator fromXmlConfig(File xmlFile) throws ConfigReadingException {
        try {
            ObjectMapper mapper = ImageGenerator.configureMapperUsingProperty(new XmlMapper());
            return mapper.readValue(xmlFile, ImageGenerator.class);
        } catch (IOException exc) {
            throw new ConfigReadingException("Cannot generate image generator from XML file: " + xmlFile, exc);
        }
    }

    /**
     * Builds an image generator instance starting from a JSON file.
     * <p>
     * JSON format and specifications varies according to the used layer.
     *
     * @param jsonFile the file to use to build the image generator
     * @return the image generator instance, pre-configured according to the input file
     * @throws ConfigReadingException when the file is unreadable or malformed
     */
    public static ImageGenerator fromJsonConfig(File jsonFile) throws ConfigReadingException {
        try {
            ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper());
            return mapper.readValue(jsonFile, ImageGenerator.class);
        } catch (IOException exc) {
            throw new ConfigReadingException("Cannot generate image generator from JSON file: " + jsonFile, exc);
        }
    }

    /**
     * Builds an image generator instance starting from a YAML file.
     * <p>
     * YAML format and specifications varies according to the used layer.
     *
     * @param yamlFile the file to use to build the image generator
     * @return the image generator instance, pre-configured according to the input file
     * @throws ConfigReadingException when the file is unreadable or malformed
     */
    public static ImageGenerator fromYamlConfig(File yamlFile) throws ConfigReadingException {
        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
            yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
            yamlFactory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
            ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper(yamlFactory));
            return mapper.readValue(yamlFile, ImageGenerator.class);
        } catch (IOException exc) {
            throw new ConfigReadingException("Cannot generate image generator from YAML file: " + yamlFile, exc);
        }
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

    /**
     * Adds a layer on top of the existing layers stack.
     * <p>
     * This method uses the builder pattern.
     *
     * @param layer the layer to be added
     * @return the image generator instance with the added layer
     */
    public ImageGenerator addLayer(Layer layer) {
        this.layers.add(layer);
        return this;
    }

    public int getCanvasWidth() {
        return this.canvasWidth;
    }

    public int getCanvasHeight() {
        return this.canvasHeight;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public boolean hasAlphaChannel() {
        return this.hasAlphaChannel;
    }

    /**
     * Dumps the existing stack and configuration into an XML file for subsequent use or for debugging purposes
     *
     * @param xmlFile the destination file
     * @throws ConfigWritingException when the file s not writable (denied permissions or non-existing path)
     */
    public void toXmlConfig(File xmlFile) throws ConfigWritingException {
        try {
            XmlMapper mapper = (XmlMapper) ImageGenerator.configureMapperUsingProperty(new XmlMapper());
            mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            mapper.writeValue(xmlFile, this);
        } catch (IOException exc) {
            throw new ConfigWritingException("Cannot dump configuration to XML file: " + xmlFile, exc);
        }
    }

    /**
     * Dumps the existing stack and configuration into a JSON file for subsequent use or for debugging purposes
     *
     * @param jsonFile the destination file
     * @throws ConfigWritingException when the file s not writable (denied permissions or non-existing path)
     */
    public void toJsonConfig(File jsonFile) throws IOException {
        try {
            ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper());
            mapper.writeValue(jsonFile, this);
        } catch (IOException exc) {
            throw new ConfigWritingException("Cannot dump configuration to JSON file: " + jsonFile, exc);
        }
    }

    /**
     * Dumps the existing stack and configuration into a YAML file for subsequent use or for debugging purposes
     *
     * @param yamlFile the destination file
     * @throws IOException when the file s not writable (denied permissions or non-existing path)
     */
    public void toYamlConfig(File yamlFile) throws IOException {
        try {
            YAMLFactory yamlFactory = new YAMLFactory();
            yamlFactory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
            yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
            yamlFactory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
            ObjectMapper mapper = ImageGenerator.configureMapperUsingWrapperObject(new ObjectMapper(yamlFactory));
            mapper.writeValue(yamlFile, this);
        } catch (IOException exc) {
            throw new ConfigWritingException("Cannot dump configuration to YAML file: " + yamlFile, exc);
        }
    }

    /**
     * Combines the existing layers ang internally generates the final output image.
     * <p>
     * Since the generation process can be long (depending on the image size and layers number and type),
     * the image generation can be called lazily only when the final output is required.
     * <p>
     * The image generation process is non-destructive, i.e. if after the first generation the user wants
     * to add another layer and re-generate the image, existing layers will e preserved.
     *
     * @return the image generator instance, using builder pattern
     * @throws ImageGenerationException if there are errors during image generation
     */
    public ImageGenerator build() throws ImageGenerationException {
        if (this.layers.isEmpty()) {
            throw new ImageGenerationException("No layer has been added");
        }

        for (Layer layer : this.layers) {
            this.processedImage = layer.apply(this.processedImage);
        }
        return this;
    }

    /**
     * Returns the processed image as a Java buffered image (for internal use
     *
     * @return the processed image as a buffered image
     * @throws ImageGenerationException when the final image has not been processed before or if no layers have been added
     */
    public BufferedImage toImage() throws ImageGenerationException {
        return this.safelyGetProcessedImage();
    }

    /**
     * Returns the processed image as a byte array for low-level operations
     *
     * @param format the underlying image format
     * @return the processed image as a byte array
     * @throws ImageWritingException when the byte array is not writable or
     *                               if there are some errors during image processing
     */
    public byte[] toByteArray(ImageFormat format) throws ImageWritingException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(this.safelyGetProcessedImage(), format.getExtension(), outputStream);
            return outputStream.toByteArray();
        } catch (IOException exc) {
            throw new ImageWritingException("Cannot write to output byte array", exc);
        }
    }

    /**
     * Writes the processed image to a file
     *
     * @param file   the output file
     * @param format the file format
     * @return whether the file has been written or not
     * @throws ImageWritingException if there are errors when writing the file or
     *                               if there are some errors during image processing
     */
    public boolean toFile(File file, ImageFormat format) throws ImageWritingException {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            return ImageIO.write(this.safelyGetProcessedImage(), format.getExtension(), outputStream);
        } catch (IOException exc) {
            throw new ImageWritingException("Cannot write to output file", exc);
        }
    }

    /**
     * Returns the Base64 representation of the processed image
     *
     * @param format the underlying image format
     * @return the Base64 representation of the processed image
     * @throws ImageWritingException if there are some errors during the processed image generation
     */
    public String toBase64(ImageFormat format) throws ImageWritingException {
        byte[] byteArray = this.toByteArray(format);
        return Base64.getEncoder().encodeToString(byteArray);
    }

    /**
     * Returns the data URL representation of the processed image
     * <p>
     * Data URLs are composed of the MIME type of the image and the Base64 representation of the image
     *
     * @param format the underlying image format
     * @return the data URL representation of the processed image
     * @throws ImageWritingException if there are some errors during the processed image generation
     * @link https://en.wikipedia.org/wiki/Data_URI_scheme
     */
    public String toDataUrl(ImageFormat format) throws ImageWritingException {
        String base64image = this.toBase64(format);
        String mimeType = format.getMimeType();
        return String.format("data:%s;base64,%s", mimeType, base64image);
    }

    private BufferedImage safelyGetProcessedImage() throws ImageGenerationException {
        if (Objects.isNull(this.processedImage)) {
            throw new ImageGenerationException(
                    "No image was processed. " +
                            "Ensure that at least one layer is set " +
                            "and that the build() method has been executed at least once " +
                            "before accessing the processed image"
            );
        }

        return this.processedImage;
    }

    private void fillWithBackgroundColor(Color color) {
        Graphics2D graphics2D = this.processedImage.createGraphics();
        graphics2D.setBackground(color);
        graphics2D.clearRect(0, 0, this.processedImage.getWidth(), this.processedImage.getHeight());
        graphics2D.dispose();
    }
}
