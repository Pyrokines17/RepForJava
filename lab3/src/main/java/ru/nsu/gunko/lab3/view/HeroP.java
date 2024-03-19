package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import ru.nsu.gunko.lab3.model.Side;
import java.io.InputStream;
import java.util.*;

public class HeroP implements Person {
    private final List<Image> list;
    private final ImageView hero;

    public HeroP(StackPane stackPane) {
        list = new ArrayList<>();

        addImage("hero/hero.png");
        addImage("hero/hero1.png");
        addImage("hero/heroAlt.png");

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(156);
        hero.setFitWidth(130);
        stackPane.getChildren().add(hero);
    }

    private void addImage(String name) {
        InputStream inputStream = getClass().getResourceAsStream(name);
        assert inputStream != null;
        Image image = new Image(inputStream);
        list.add(image);
    }

    @Override
    public void move(int x, int y) {
        hero.setTranslateX(x);
        hero.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, Side side) {
    }

    @Override
    public void changeImage(Side side) {
        if (side.equals(Side.RIGHT)) {
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
