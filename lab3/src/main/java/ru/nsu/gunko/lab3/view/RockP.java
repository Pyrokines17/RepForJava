package ru.nsu.gunko.lab3.view;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import ru.nsu.gunko.lab3.model.Side;
import java.io.InputStream;

public class RockP implements Person{
    private final ImageView rock;

    public RockP(StackPane stackPane) {
        int height = 120,
                width = 120;

        InputStream inputStream = getClass().getResourceAsStream("obj/rock.png");
        assert inputStream != null;
        Image image = new Image(inputStream);

        rock = new ImageView(image);
        rock.setFitHeight(height);
        rock.setFitWidth(width);
        stackPane.getChildren().add(rock);
    }

    @Override
    public void move(int x, int y) {
        rock.setTranslateX(x);
        rock.setTranslateY(y);
    }

    @Override
    public void action(int x, int y, Side side) {
    }

    @Override
    public void changeImage(Side side) {
    }

    @Override
    public void deleteImage() {
        rock.setVisible(false);
    }
}
