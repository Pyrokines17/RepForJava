package ru.nsu.gunko.lab3.view;

import javafx.animation.PauseTransition;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import ru.nsu.gunko.lab3.model.Side;
import java.io.InputStream;
import java.util.*;

public class SkeletonP implements Person {
    private final List<Image> list;
    private final ImageView skeleton;
    private final ImageView splash;

    public SkeletonP(StackPane stackPane) {
        list = new ArrayList<>();
        int height = 156,
                width = 130;

        addImage("skeleton/skeleton.png");
        addImage("skeleton/skeleton1.png");
        addImage("skeleton/splash.png");
        addImage("skeleton/splash1.png");

        splash = new ImageView(list.get(2));
        splash.setFitHeight(height);
        splash.setFitWidth(width);
        stackPane.getChildren().add(splash);
        splash.setVisible(false);

        skeleton = new ImageView(list.getFirst());
        skeleton.setFitHeight(height);
        skeleton.setFitWidth(width);
        stackPane.getChildren().add(skeleton);
    }

    private void addImage(String name) {
        InputStream inputStream = getClass().getResourceAsStream(name);
        assert inputStream != null;
        Image image = new Image(inputStream);
        list.add(image);
    }

    @Override
    public void move(int x, int y) {
        skeleton.setTranslateX(x);
        skeleton.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, Side side) {
        if (side.equals(Side.RIGHT)) {
            splash.setImage(list.get(2));
        } else {
            splash.setImage(list.get(3));
        }

        splash.setTranslateX(x);
        splash.setTranslateY(y);

        splash.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(0.5));
        visiblePause.setOnFinished(event -> splash.setVisible(false));
        visiblePause.play();
    }

    @Override
    public void changeImage(Side side) {
        if (side.equals(Side.RIGHT)) {
            skeleton.setImage(list.getFirst());
        } else {
            skeleton.setImage(list.get(1));
        }
    }

    @Override
    public void deleteImage() {
        skeleton.setVisible(false);
    }
}
