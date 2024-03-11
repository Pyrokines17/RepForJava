package ru.nsu.gunko.lab3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Stage;
import ru.nsu.gunko.lab3.controller.KeyInput;
import ru.nsu.gunko.lab3.model.Context;
import ru.nsu.gunko.lab3.view.Window;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Window window = new Window();
        window.init(stage);
        window.setBackground();

        Context context = new Context(window);
        Scene scene = new Scene(window.getStackPane(), 1640, 1010);
        KeyInput keyInput = new KeyInput(context);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> keyInput.entry(keyEvent.getCode()));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}