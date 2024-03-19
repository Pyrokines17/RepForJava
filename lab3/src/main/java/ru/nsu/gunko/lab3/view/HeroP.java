package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.util.*;

public class HeroP implements Person {
    private final List<Image> list;
    private final ImageView hero;

    public HeroP(StackPane stackPane) {
        list = new ArrayList<>();
        InputStream inputStream = getClass().getResourceAsStream("hero/hero.png");
        assert inputStream != null;
        Image image = new Image(inputStream);
        list.add(image);

        InputStream inputStream1 = getClass().getResourceAsStream("hero/hero1.png");
        assert inputStream1 != null;
        Image image1 = new Image(inputStream1);
        list.add(image1);

        InputStream inputStreamAlt = getClass().getResourceAsStream("hero/heroAlt.png");
        assert inputStreamAlt != null;
        Image imageAlt = new Image(inputStreamAlt);
        list.add(imageAlt);

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(156);
        hero.setFitWidth(130);
        stackPane.getChildren().add(hero);
    }

    @Override
    public void move(int x, int y) {
        hero.setTranslateX(x);
        hero.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, String side) {
    }

    @Override
    public void changeImage(String side) {
        if (side.equals("right")) {
            hero.setImage(list.getFirst());
        } else {
            hero.setImage(list.get(1));
        }
    }

    @Override
    public void deleteImage() {
        hero.setVisible(false);
    }
}
