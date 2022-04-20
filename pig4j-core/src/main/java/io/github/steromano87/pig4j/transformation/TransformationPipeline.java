package io.github.steromano87.pig4j.transformation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TransformationPipeline implements Transformation {
    List<Transformation> transformations = new ArrayList<>();

    public void addTransformation(Transformation transformation) {
        this.transformations.add(transformation);
    }

    @Override
    public BufferedImage transform(BufferedImage inputImage) {
        BufferedImage intermediateImage = inputImage;

        for (Transformation transformation : this.transformations) {
            intermediateImage = transformation.transform(intermediateImage);
        }

        return intermediateImage;
    }
}
