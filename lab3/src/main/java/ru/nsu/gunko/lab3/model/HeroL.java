package ru.nsu.gunko.lab3.model;

import java.util.*;

public class HeroL implements Logic {
    private final List<Integer> coordinates;
    private String side;
    private final Model model;
    private final String name;
    private final int id;

    public HeroL(Model newModel) {
        coordinates = new ArrayList<>();
        coordinates.add(0); coordinates.add(333);
        side = "right";
        name = "hero";
        model = newModel;
        id = model.getObj().size();
    }

    @Override
    public void move(String side1) {
        int flagCI = 0,
                step = 15;

        switch (side1) {
            case "up" : {
                coordinates.set(1, Math.max(coordinates.get(1)-step, -400));
                //side = side.equals("right") ? "left" : "right";
                //flagCI = 1;
                break;}
            case "down" : {
                coordinates.set(1, Math.min(coordinates.get(1)+step, 333));
                //side = side.equals("right") ? "left" : "right";
                //flagCI = 1;
                break;}
            case "left" : {
                coordinates.set(0, Math.max(coordinates.getFirst()-step, -680));
                if (side.equals("right")) {
                    side = "left";
                    flagCI = 1;
                }
                break;}
            case "right" : {
                coordinates.set(0, Math.min(coordinates.getFirst()+step, 680));
                if (side.equals("left")) {
                    side = "right";
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
        //ToDo: shooting
    }

    @Override
    public List<Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public String getSide() {
        return side;
    }

    @Override
    public String getName() {
        return name;
    }
}
