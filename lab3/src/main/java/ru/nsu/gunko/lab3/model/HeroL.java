package ru.nsu.gunko.lab3.model;

public class HeroL implements Logic {
    private final Model model;
    private int hp = 150;
    private int id;
    private Side side;
    private int x = 0;
    private int y = 333;

    public HeroL(Model newModel) {
        side = Side.RIGHT;
        model = newModel;
    }

    @Override
    public void move(String side1) {
        int flagCI = 0,
                step = 15;

        int borderX = 680,
                borderY = 333;

        switch (side1) {
            case "up" : {
                y = Math.max(y-step, -400);
                break;}
            case "down" : {
                y = Math.min(y+step, borderY);
                break;}
            case "left" : {
                x = Math.max(x-step, -borderX);
                if (side.equals(Side.RIGHT)) {
                    side = Side.LEFT;
                    flagCI = 1;
                }
                break;}
            case "right" : {
                x= Math.min(x+step, borderX);
                if (side.equals(Side.LEFT)) {
                    side = Side.RIGHT;
                    flagCI = 1;
                }
                break;}
        }

        model.setState(State.MOVE);
        model.signal(id);

        if (flagCI == 1) {
            model.setState(State.CHANGE_IMAGE);
            model.signal(id);
        }
    }

    @Override
    public void action(String parameter) {
        if (parameter.equals("left")) {
            model.getObj().add(new BulletL(model, Side.LEFT, x, y));
        } else {
            model.getObj().add(new BulletL(model, Side.RIGHT, x, y));
        }

        model.getObj().getLast().setId(model.getObj().size()-1);
        model.setState(State.INIT_IMAGE);
        model.signal(model.getObj().size()-1);
    }

    @Override
    public int delete() {
        if (hp <= 0) {
            model.setState(State.DELETE_IMAGE);
            model.signal(id);
            model.removeHero();
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
        return "hero";
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
