package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.util.*;

public class SkeletonP implements Person {
    private final List<Image> list;
    private final ImageView skeleton;

    public SkeletonP(StackPane stackPane) {
        list = new ArrayList<>();
        InputStream inputStream = getClass().getResourceAsStream("skeleton/skeleton.png");
        assert inputStream != null;
        Image image = new Image(inputStream);

        InputStream inputStream1 = getClass().getResourceAsStream("skeleton/skeleton1.png");
        assert inputStream1 != null;
        Image image1 = new Image(inputStream1);
        list.add(image); list.add(image1);

        skeleton = new ImageView(list.getFirst());
        skeleton.setFitHeight(156);
        skeleton.setFitWidth(130);
        stackPane.getChildren().add(skeleton);
    }

    @Override
    public void move(int x, int y) {
        skeleton.setTranslateX(x);
        skeleton.setTranslateY(y);
    }

    @Override
    public void action() {
        //ToDo: action
    }

    @Override
    public void changeImage(String side) {
        if (side.equals("right")) {
            skeleton.setImage(list.getFirst());
        } else {
            skeleton.setImage(list.get(1));
        }
    }
}
