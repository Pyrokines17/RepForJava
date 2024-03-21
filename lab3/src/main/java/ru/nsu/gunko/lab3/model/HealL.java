package ru.nsu.gunko.lab3.model;

import java.util.Random;

public class HealL implements Logic {
    private final Model model;
    private int hp = 1;
    private int id;
    private final Side side;
    private final int x;
    private final int y;

    public HealL(Model newModel) {
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
        int difBorder = 30;
        int heroX = model.getHero().getX(),
                heroY = model.getHero().getY();
        int difX = heroX > x ? heroX-x : x-heroX,
                difY = heroY > y ? heroY-y : y-heroY;

        if (difX < difBorder && difY < difBorder) {
            model.getHero().changeHP(30);
            hp -= 15;
        }
    }

    @Override
    public boolean delete() {
        if (hp <= 0) {
            model.setState(State.DELETE_IMAGE);
            model.signal(id);
            model.getObj().remove(id);
            return true;
        }
        return false;
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
        return "heal";
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
