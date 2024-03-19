package ru.nsu.gunko.lab3.view;

public interface Person {
    void move(int x, int y);

    void action(int x, int y, String side);

    void changeImage(String side);

    void deleteImage();
}
