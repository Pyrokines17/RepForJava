package ru.nsu.gunko.lab3.view;

import java.util.*;
import java.io.InputStream;
import javafx.scene.image.*;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.animation.PauseTransition;
import ru.nsu.gunko.lab3.model.Side;

public class HeroP implements Person {
    private final List<Image> list;
    private final ImageView hero;

    public HeroP(StackPane stackPane) {
        list = new ArrayList<>();
        int height = 156,
                width = 130;

        addImage("hero/hero.png");
        addImage("hero/hero1.png");
        addImage("hero/heroAlt.png");

        hero = new ImageView(list.getFirst());
        hero.setFitHeight(height);
        hero.setFitWidth(width);
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
        int choose = side.equals(Side.RIGHT) ? 0 : 1;
        hero.setImage(list.get(2));
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(0.5));
        visiblePause.setOnFinished(event -> hero.setImage(list.get(choose)));
        visiblePause.play();
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
    public void deleteImage(StackPane stackPane) {
        hero.setVisible(false);
    }
}
