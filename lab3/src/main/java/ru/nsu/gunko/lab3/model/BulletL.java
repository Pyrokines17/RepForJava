package ru.nsu.gunko.lab3.model;

public class BulletL implements Logic {
    private final Model model;
    private int hp = 2;
    private int id;
    private final Side side;
    private int x;
    private final int y;

    public BulletL(Model newModel, Side newSide, int newX, int newY) {
        x = newX;
        y = newY;

        side = newSide;
        model = newModel;
    }

    @Override
    public void move(String side1) {
        int step = 20;

        if (side.equals(Side.RIGHT)) {
            x += step;
        } else {
            x -= step;
        }

        model.setState(State.MOVE);
        model.signal(id);
    }

    @Override
    public void action(String parameter) {
        int borderX = 680;
        if (x > borderX || x < -borderX) {
            hp -= 15;
        }
        //ToDo: change hp
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
        return "bullet";
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
