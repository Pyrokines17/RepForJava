package ru.nsu.gunko.lab3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ru.nsu.gunko.lab3.controller.KeyInput;
import ru.nsu.gunko.lab3.view.Window;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Window window = new Window();
        window.init(stage);
        Background background = window.getBackground();

        StackPane stackPane = new StackPane();
        stackPane.setBackground(background);
        Scene scene = new Scene(stackPane, 1643, 1012);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}