package io.github.steromano87.pig4j;

import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NoOpLayer implements Layer {
    private Duration returnDelay = Duration.ZERO;

    public void setReturnDelay(Duration delay) {
        this.returnDelay = delay;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        try {
            TimeUnit.MILLISECONDS.sleep(this.returnDelay.toMillis());
            return image;
        } catch (InterruptedException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoOpLayer noOpLayer = (NoOpLayer) o;

        return returnDelay.equals(noOpLayer.returnDelay);
    }

    @Override
    public int hashCode() {
        return returnDelay.hashCode();
    }
}
