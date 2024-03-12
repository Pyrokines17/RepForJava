package ru.nsu.gunko.lab3.model;

public class Model {
    private ModelListener modelListener;

    private final HeroL heroL;
//-----------------------------//
    private final SkeletonL skeletonL;

    public Model() {
        heroL = new HeroL(this);
//-----------------------------//
        skeletonL = new SkeletonL(this);
    }

    public HeroL getHero() {
        return heroL;
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
