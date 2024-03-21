package ru.nsu.gunko.lab3.view;

import javafx.scene.layout.StackPane;
import ru.nsu.gunko.lab3.model.Side;

public interface Person {
    void move(int x, int y);

    void action(int x, int y, Side side);

    void changeImage(Side side);

    void deleteImage(StackPane stackPane);

    void setId(int id);
}
