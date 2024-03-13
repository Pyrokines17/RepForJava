package ru.nsu.gunko.lab3.model;

import java.util.List;

public interface Logic {
    void move(String side);

    void action(String parameter);

    String getSide();

    List<Integer> getCoordinates();

    String getName();
}
