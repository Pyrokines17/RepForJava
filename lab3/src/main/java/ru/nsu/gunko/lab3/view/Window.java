package ru.nsu.gunko.lab3.view;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.InputStream;

public class Window {
    private final StackPane stackPane;
    private final HeroP heroP;

    public Window() {
        stackPane = new StackPane();
        heroP = new HeroP(stackPane);
    }

    public void init(Stage stage) {
        stage.setTitle("New Game!");
        stage.setResizable(false);
        InputStream iconStream = getClass().getResourceAsStream("icon.png");
        assert iconStream != null;
        Image imageOfIcon = new Image(iconStream);
        stage.getIcons().add(imageOfIcon);
    }

    public void setBackground() {
        InputStream bgStream = getClass().getResourceAsStream("back.png");
        assert bgStream != null;
        Image imageOfBg = new Image(bgStream);
        BackgroundImage backgroundImage = new BackgroundImage(imageOfBg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        stackPane.setBackground(new Background(backgroundImage));
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public HeroP getHeroP() {
        return heroP;
    }
}
