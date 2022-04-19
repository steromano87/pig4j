package io.github.steromano87.pig4j.cache;

import io.github.steromano87.pig4j.exceptions.Pig4jException;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class CacheManager {
    private Integer configurationHash;
    private byte[] inputImageHash;

    @Setter
    @Getter
    private BufferedImage cachedImage;

    public void refreshConfigurationHash(int configurationHash) {
        this.configurationHash = configurationHash;
    }

    public void refreshInputImageHash(BufferedImage inputImage) {
        this.inputImageHash = calculateImageHash(inputImage);
    }

    public boolean isCachedImageInvalid(int currentConfigurationHash, BufferedImage currentInputImage) {
        return Objects.isNull(this.inputImageHash) ||
                Objects.isNull(this.configurationHash) ||
                currentConfigurationHash != this.configurationHash ||
                !Arrays.equals(calculateImageHash(currentInputImage), this.inputImageHash);
    }

    private static byte[] calculateImageHash(BufferedImage image) throws Pig4jException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(imageBytes);
            return md.digest();
        } catch (IOException | NoSuchAlgorithmException exc) {
            throw new Pig4jException("Cannot calculate image hash", exc);
        }
    }
}
