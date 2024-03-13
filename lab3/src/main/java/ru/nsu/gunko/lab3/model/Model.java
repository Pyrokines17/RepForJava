package ru.nsu.gunko.lab3.model;

import java.util.*;

public class Model {
    private ModelListener modelListener;
    private State state = State.NOTHING;
    private final List<Logic> gameObj;

    public Model() {
        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));
        gameObj.add(new SkeletonL(this));
    }

    public List<Logic> getObj() {
        return gameObj;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void setListener(ModelListener newListener) {
        modelListener = newListener;
    }

    public void signal() {
        if (modelListener != null) {
            modelListener.reaction();
        }
    }
}
