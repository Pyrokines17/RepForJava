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
        Logic per;
        int difX, difY, newX, newY;
        int border = 40;

        for (int i = 0; i < model.getCountOfEnemy() + 1; ++i) {
            per = model.getObj().get(i);

            difX = x - per.getX();
            difY = y - per.getY();

            if ( ((difY > 0 && difY <= border) || (difY < 0 && difY >= -border)) &&
                    ((difX > 0 && difX <= border) || (difX < 0 && difX >= -border)) ) {
                newX = (difX > 0) ? (x-border) : (x+border);
                model.getObj().get(i).setX(newX);
                newY = (difY > 0) ? (y-border) : (y+border);
                model.getObj().get(i).setY(newY);
            }

        }
    }

    @Override
    public boolean delete() {
        if (hp <= 0) {
            model.setState(State.DELETE_IMAGE);
            model.signal(id);
            model.getObj().remove(id);
            model.removeRock();
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

    @Override
    public void setX(int x) {
    }

    @Override
    public void setY(int y) {
    }

    @Override
    public int getHp() {
        return hp;
    }
}
