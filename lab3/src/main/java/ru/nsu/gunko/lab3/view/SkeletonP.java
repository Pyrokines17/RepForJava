package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.util.List;

public class SkeletonP implements Person {
    private final ImageView skeleton;

    public SkeletonP(StackPane stackPane) {
        InputStream inputStream = getClass().getResourceAsStream("skeleton.png");
        assert inputStream != null;
        Image image = new Image(inputStream);

        skeleton = new ImageView(image);
        skeleton.setFitHeight(156);
        skeleton.setFitWidth(130);
        stackPane.getChildren().add(skeleton);
    }

    public void listener(int signal, List<Integer> params) {

    }
}
