package ru.nsu.gunko.lab3;

import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ru.nsu.gunko.lab3.controller.PlatformHelper;
import ru.nsu.gunko.lab3.view.*;
import ru.nsu.gunko.lab3.model.Model;
import ru.nsu.gunko.lab3.controller.Controller;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Window window = new Window();
        window.init(stage);
        BackgroundImage backgroundImage = window.getBackground();

        Model model = new Model();
        View view = new View(model);
        model.setListener(view);
        view.getStackPane().setBackground(new Background(backgroundImage));

        Scene scene = new Scene(view.getStackPane(), 1640, 1010);
        Controller controller = new Controller(model);
        controller.playMusic();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> controller.entry(keyEvent.getCode()));

        stage.setScene(scene);

        PlatformHelper.run(stage::show);
    }

    public static void main(String[] args) {
        launch();
    }
}