package ru.nsu.gunko.lab3.model;

import java.util.*;

public class Model {
    private ModelListener modelListener;
    private State state = State.NOTHING;
    private final List<Logic> gameObj;
    private int count;

    public Model() {
        count = 0;
        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));
        ++count;
        gameObj.add(new SkeletonL(this));
        ++count;
    }

    public List<Logic> getObj() {
        return gameObj;
    }

    public State getState() {
        return state;
    }

    public int getCount() {
        return count;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void setListener(ModelListener newListener) {
        modelListener = newListener;
    }

    public void signal(int id) {
        if (modelListener != null) {
            modelListener.reaction(id);
        }
    }
}
