package ru.nsu.gunko.lab3.model;

import java.util.*;

public class SkeletonL implements Logic{
    private final List<Integer> coordinates;
    private final String side;
    private final Model model;
    private final String name;
    private final int id;

    public SkeletonL(Model newModel) {
        Random r = new Random();
        coordinates = new ArrayList<>();
        coordinates.add(r.nextInt()); coordinates.add(r.nextInt());
        side = "right";
        name = "skeleton";
        model = newModel;
        id = model.getCount();
    }

    @Override
    public void move(String side) {
        //ToDo: moving
    }

    @Override
    public void action(String parameter) {
        //ToDo: action
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
