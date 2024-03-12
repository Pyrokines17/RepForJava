package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.util.*;

public class HeroP implements Person {
    private final List<Image> list;
    private final ImageView hero;

    public HeroP(StackPane stackPane) {
        InputStream inputStream = getClass().getResourceAsStream("hero.png");
        InputStream inputStream1 = getClass().getResourceAsStream("hero1.png");
        InputStream inputStreamAlt = getClass().getResourceAsStream("heroAlt.png");

        assert inputStream != null;
        assert inputStream1 != null;
        assert inputStreamAlt != null;

        list = new ArrayList<>();
        Image image = new Image(inputStream);
        Image image1 = new Image(inputStream1);
        Image imageAlt = new Image(inputStreamAlt);
        list.add(image); list.add(image1); list.add(imageAlt);

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(156);
        hero.setFitWidth(130);
        stackPane.getChildren().add(hero);
    }

    public void listener(int signal, List<Integer> params) {
        switch (signal) {
            case 0 : {
                move(params.get(0), params.get(1));
                break;}
            case 1 : {
                changeImage(params.get(2));
                break;}
        }
    }

    private void move(int x, int y) {
        hero.setTranslateX(x);
        hero.setTranslateY(y);
    }

    private void changeImage(int side) {
        hero.setImage(list.get(side));
    }
}
