package ru.nsu.gunko.lab3.model;

import java.util.*;

public class HeroL implements Logic {
    private final List<Integer> coordinates;
    private String side;
    private final Model model;

    public HeroL(Model newModel) {
        coordinates = new ArrayList<>();
        coordinates.add(0); coordinates.add(0);
        side = "right";
        model = newModel;
    }

    public void move(String side1) {
        int flagCI = 0;

        switch (side1) {
            case "up" : {
                coordinates.set(1, coordinates.get(1)-10);
                side = side.equals("right") ? "left" : "right";
                flagCI = 1;
                break;}
            case "down" : {
                coordinates.set(1, coordinates.get(1)+10);
                side = side.equals("right") ? "left" : "right";
                flagCI = 1;
                break;}
            case "left" : {
                coordinates.set(0, coordinates.getFirst()-10);
                if (side.equals("right")) {
                    side = "left";
                    flagCI = 1;
                }
                break;}
            case "right" : {
                coordinates.set(0, coordinates.getFirst()+10);
                if (side.equals("left")) {
                    side = "right";
                    flagCI = 1;
                }
                break;}
        }

        model.setState(State.MOVE);
        model.signal();

        if (flagCI == 1) {
            model.setState(State.CHANGE_IMAGE);
            model.signal();
        }
    }

    public void action(String parameter) {
        //ToDo: shooting
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public String getSide() {
        return side;
    }
}
