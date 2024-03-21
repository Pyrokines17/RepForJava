package ru.nsu.gunko.lab3.model;

public interface Logic {
    void move(String side);

    void action(String parameter);

    Side getSide();

    int getX();

    int getY();

    String getName();

    boolean delete();

    void changeHP(int number);

    void setId(int id);
}
