package ru.nsu.gunko.lab3.view;

import javafx.animation.PauseTransition;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.*;

public class SkeletonP implements Person {
    private final List<Image> list;
    private final ImageView skeleton;
    private final ImageView splash;

    public SkeletonP(StackPane stackPane) {
        list = new ArrayList<>();

        addImage("skeleton/skeleton.png");
        addImage("skeleton/skeleton1.png");
        addImage("skeleton/splash.png");
        addImage("skeleton/splash1.png");

        splash = new ImageView(list.get(2));
        splash.setFitHeight(156);
        splash.setFitWidth(130);
        stackPane.getChildren().add(splash);
        splash.setVisible(false);

        skeleton = new ImageView(list.getFirst());
        skeleton.setFitHeight(156);
        skeleton.setFitWidth(130);
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
    public void action(int x, int y, String side) {
        if (side.equals("right")) {
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
    public void changeImage(String side) {
        if (side.equals("right")) {
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
