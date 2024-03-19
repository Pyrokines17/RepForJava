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
        if (side.equals(Side.RIGHT)) {
            x += 10;
        } else {
            x -= 10;
        }

        model.setState(State.MOVE);
        model.signal(id);
    }

    @Override
    public void action(String parameter) { //ToDo: fix
        int borderX = 680;
        if (x > borderX || x < -borderX) {
            hp -= 15;
        }

        int othX,
                othY;
        int difX,
                difY;

        int countOfObj = model.getObj().size();
        for (int i = 1; i < countOfObj; ++i) {
            othX = model.getObj().get(i).getX();
            othY = model.getObj().get(i).getY();

            difX = othX > x ? othX-x : x-othX;
            difY = othY > y ? othY-y : y-othY;

            if (difX < 5 && difY < 10) {
                hp -= 15;
                model.getObj().get(i).changeHP(-15);
            }
        }
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
