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

        InputStream inputStream1 = getClass().getResourceAsStream("hero/hero1.png");
        assert inputStream1 != null;
        Image image1 = new Image(inputStream1);

        InputStream inputStreamAlt = getClass().getResourceAsStream("hero/heroAlt.png");
        assert inputStreamAlt != null;
        Image imageAlt = new Image(inputStreamAlt);
        list.add(image); list.add(image1); list.add(imageAlt);

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(156);
        hero.setFitWidth(130);
        stackPane.getChildren().add(hero);
    }

    public void move(int x, int y) {
        hero.setTranslateX(x);
        hero.setTranslateY(y);
    }
}
