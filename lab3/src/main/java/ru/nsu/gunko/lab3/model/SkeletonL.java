package ru.nsu.gunko.lab3.model;

import java.util.*;

public class SkeletonL implements Logic {
    private final Model model;
    private int hp = 30;
    private int id;
    private Side side;
    private int x;
    private int y;
    private final int borderX = 680;
    private final int borderY = 333;

    public SkeletonL(Model newModel) {
        Random rand = new Random();
        x = rand.nextInt() % borderX;
        y = rand.nextInt() % borderY;

        side = Side.RIGHT;
        model = newModel;
    }

    @Override
    public void move(String side1) {
        int heroX = model.getHero().getX(),
                heroY = model.getHero().getY();
        int step = 5,
                flagMove = 0,
                flagCI = 0;
        int temp = heroX - x;

        if (temp > 0) {
            x = Math.min(x+step, borderX);
            if (side.equals(Side.LEFT)) {
                side = Side.RIGHT;
                flagCI = 1;
            }
            flagMove = 1;
        } else if (temp < 0) {
            x = Math.max(x-step, -borderX);
            if (side.equals(Side.RIGHT)) {
                side = Side.LEFT;
                flagCI = 1;
            }
            flagMove = 1;
        }

        int temp1 = heroY - y;
        if (temp1 > 0) {
            y = Math.min(y+step, borderY);
            flagMove = 1;
        } else if (temp1 < 0) {
            y = Math.max(y-step, -400);
            flagMove = 1;
        }

        if (flagMove == 1) {
            model.setState(State.MOVE);
            model.signal(id);
        }

        if (flagCI == 1) {
            model.setState(State.CHANGE_IMAGE);
            model.signal(id);
        }
    }

    @Override
    public void action(String parameter) {
        int heroX = model.getHero().getX(),
                heroY = model.getHero().getY();
        int difX = heroX > x ? heroX-x : x-heroX,
                difY = heroY > y ? heroY-y : y-heroY;

        if (difX < 20 && difY < 10) {
            model.getHero().changeHP(-7);
            model.setState(State.ACTION);
            model.signal(id);
        }
    }

    @Override
    public int delete() {
        if (hp <= 0) {
            model.setState(State.DELETE_IMAGE);
            model.signal(id);
            model.getObj().remove(id);
            model.removeEnemy();
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
        return "skeleton";
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
