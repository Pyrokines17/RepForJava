package ru.nsu.gunko.lab3.view;

import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import java.io.InputStream;

public class Window {
    public void init(Stage stage) {
        stage.setTitle("New Game!");
        stage.setResizable(false);

        InputStream iconStream = getClass().getResourceAsStream("window/icon.png");
        assert iconStream != null;
        Image imageOfIcon = new Image(iconStream);
        stage.getIcons().add(imageOfIcon);
    }

    public BackgroundImage getBackground() {
        InputStream bgStream = getClass().getResourceAsStream("window/back.png");
        assert bgStream != null;
        Image imageOfBg = new Image(bgStream);
        return new BackgroundImage(imageOfBg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    }
}
