package ru.nsu.gunko.lab3.model;

import java.util.*;

public class SkeletonL implements Logic{
    private final List<Integer> coordinates;
    private final String side;
    private final Model model;

    public SkeletonL(Model newModel) {
        coordinates = new ArrayList<>();
        coordinates.add(0); coordinates.add(0);
        side = "right";
        model = newModel;
    }

    public void move(String side) {
        //ToDo: moving
    }

    public void action(String parameter) {
        //ToDo: action
    }
}
