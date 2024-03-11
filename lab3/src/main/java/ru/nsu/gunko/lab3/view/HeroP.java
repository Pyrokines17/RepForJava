package ru.nsu.gunko.lab3.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HeroP implements Person {
    private final List<Image> list;
    private final ImageView hero;

    public HeroP(StackPane stackPane) {
        InputStream inputStream = getClass().getResourceAsStream("hero.png");
        InputStream inputStream1 = getClass().getResourceAsStream("hero1.png");

        assert inputStream != null;
        assert inputStream1 != null;

        list = new ArrayList<>();
        Image image = new Image(inputStream);
        Image image1 = new Image(inputStream1);
        list.add(image); list.add(image1);

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(156);
        hero.setFitWidth(130);
        stackPane.getChildren().add(hero);
    }

    public void listener(int signal, List<Integer> params) {
        if (signal == 0) {
            move(params.get(0), params.get(1));
        }
    }

    private void move(int x, int y) {
        hero.setTranslateX(x);
        hero.setTranslateY(y);
    }
}
