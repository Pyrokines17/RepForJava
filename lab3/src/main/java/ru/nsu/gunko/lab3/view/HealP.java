package ru.nsu.gunko.lab3.view;

import java.io.InputStream;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import ru.nsu.gunko.lab3.model.Side;

public class HealP implements Person {
    private final ImageView heal;

    public HealP(StackPane stackPane) {
        int height = 120,
                width = 120;

        InputStream inputStream = getClass().getResourceAsStream("obj/heal.png");
        assert inputStream != null;
        Image image = new Image(inputStream);

        heal = new ImageView(image);
        heal.setFitHeight(height);
        heal.setFitWidth(width);
        stackPane.getChildren().add(heal);
    }

    @Override
    public void move(int x, int y) {
        heal.setTranslateX(x);
        heal.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, Side side) {
    }

    @Override
    public void changeImage(Side side) {
    }

    @Override
    public void deleteImage(StackPane stackPane) {
        heal.setVisible(false);
    }
}
