package ru.nsu.gunko.lab3.view;

import ru.nsu.gunko.lab3.model.Side;

public interface Person {
    void move(int x, int y);

    void action(int x, int y, Side side);

    void changeImage(Side side);

    void deleteImage();
}
