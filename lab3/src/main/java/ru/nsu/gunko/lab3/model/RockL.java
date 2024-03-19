package ru.nsu.gunko.lab3.model;

import java.util.Random;

public class RockL implements Logic {
    private final Model model;
    private int hp = 1000;
    private int id;
    private final Side side;
    private final int x;
    private final int y;

    public RockL(Model newModel) {
        Random rand = new Random();
        x = rand.nextInt() % 600;
        y = rand.nextInt() % 300;

        side = Side.RIGHT;
        model = newModel;
    }

    @Override
    public void move(String side1) {
    }

    @Override
    public void action(String parameter) {
        //ToDo: block moving
    }

    @Override
    public int delete() {
        if (hp <= 0) {
            model.setState(State.DELETE_IMAGE);
            model.signal(id);
            model.getObj().remove(id);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public String getName() {
        return "rock";
    }

    @Override
    public void changeHP(int number) {
        hp += number;
    }

    @Override
    public void setId(int newId) {
        id = newId;
    }
}
