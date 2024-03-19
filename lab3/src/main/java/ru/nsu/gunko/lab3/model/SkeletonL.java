package ru.nsu.gunko.lab3.model;

import java.util.*;
import java.util.Random;

public class SkeletonL implements Logic{
    private final List<Integer> coordinates;
    private String side;
    private final Model model;
    private final String name;
    private final int id;
    private int hp;

    public SkeletonL(Model newModel) {
        Random rand = new Random();
        coordinates = new ArrayList<>();
        coordinates.add(rand.nextInt() % 680);
        coordinates.add(rand.nextInt() % 333);
        side = "right";
        name = "skeleton";
        model = newModel;
        id = model.getObj().size();
        hp = 100;
    }

    @Override
    public void move(String side1) {
        List<Integer> coordinatesH = model.getObj().getFirst().getCoordinates();
        int step = 5,
                flagMove = 0,
                flagCI = 0;

        int temp = coordinatesH.getFirst() - coordinates.getFirst();
        if (temp > 0) {
            coordinates.set(0, Math.min(coordinates.getFirst()+step, 680));
            if (side.equals("left")) {
                side = "right";
                flagCI = 1;
            }
            flagMove = 1;
        } else if (temp < 0) {
            coordinates.set(0, Math.max(coordinates.getFirst()-step, -680));
            if (side.equals("right")) {
                side = "left";
                flagCI = 1;
            }
            flagMove = 1;
        }

        int temp1 = coordinatesH.getLast() - coordinates.getLast();
        if (temp1 > 0) {
            coordinates.set(1, Math.min(coordinates.getLast()+step, 333));
            flagMove = 1;
        } else if (temp1 < 0) {
            coordinates.set(1, Math.max(coordinates.getLast()-step, -400));
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
        List<Integer> coordinatesH = model.getObj().getFirst().getCoordinates();
        int difX = coordinatesH.getFirst() > coordinates.getFirst() ?
                coordinatesH.getFirst()-coordinates.getFirst() : coordinates.getFirst()-coordinatesH.getFirst(),
                difY = coordinatesH.getLast() > coordinates.getLast() ?
                        coordinatesH.getLast()-coordinates.getLast() : coordinates.getLast()-coordinatesH.getLast();

        if (difX < 20 && difY < 10) {
            model.getObj().getFirst().changeHP(-15);
            model.setState(State.ACTION);
            model.signal(id);
        }
    }

    @Override
    public void delete(int countOfEnemy) {
        if (hp == 0) {
            //ToDo: del
        }
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

    @Override
    public void changeHP(int number) {
        hp += number;
    }
}
