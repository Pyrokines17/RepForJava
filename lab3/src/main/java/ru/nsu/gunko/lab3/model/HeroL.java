package ru.nsu.gunko.lab3.model;

import java.util.*;

public class HeroL implements Logic {
    private final List<Integer> coordinates;
    private final String side;
    private final Model model;

    public HeroL(Model newModel) {
        coordinates = new ArrayList<>();
        coordinates.add(0); coordinates.add(0);
        side = "right";
        model = newModel;
    }

    public void move(String side) {
        switch (side) {
            case "up" : {
                coordinates.set(1, coordinates.get(1)-10);
                model.signal();
                break;}
            case "down" : {
                coordinates.set(1, coordinates.get(1)+10);
                model.signal();
                break;}
            case "left" : {
                coordinates.set(0, coordinates.getFirst()-10);
                model.signal();
                break;}
            case "right" : {
                coordinates.set(0, coordinates.getFirst()+10);
                model.signal();
                break;}
        }
    }

    public void action(String parameter) {
        //ToDo: shooting
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }
}
