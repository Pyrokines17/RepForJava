package ru.nsu.gunko.lab3.view;

import java.io.InputStream;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import ru.nsu.gunko.lab3.model.Side;

public class BulletP implements Person {
    private final ImageView bullet;

    public BulletP(StackPane stackPane, Side side) {
        int height = 60,
                width = 60;

        InputStream inputStream;

        if (side.equals(Side.RIGHT)) {
            inputStream = getClass().getResourceAsStream("obj/bullet.png");
        } else {
            inputStream = getClass().getResourceAsStream("obj/bullet1.png");
        }

        assert inputStream != null;
        Image image = new Image(inputStream);

        bullet = new ImageView(image);
        bullet.setFitHeight(height);
        bullet.setFitWidth(width);
        stackPane.getChildren().add(bullet);
    }

    @Override
    public void move(int x, int y) {
        bullet.setTranslateX(x);
        bullet.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, Side side) {
    }

    @Override
    public void changeImage(Side side) {
    }

    @Override
    public void deleteImage(StackPane stackPane) {
        bullet.setVisible(false);
    }
}
