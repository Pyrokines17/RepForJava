package ru.nsu.gunko.lab3.model;

import ru.nsu.gunko.lab3.view.Window;

public class Context {
    private final Window window;
    private final HeroL heroL;
    private final SkeletonL skeletonL;

    public Context(Window newWindow) {
        window = newWindow;
        heroL = new HeroL(window.getHeroP());
        skeletonL = new SkeletonL(window.getSkeletonP());
    }

    public HeroL getHero() {
        return heroL;
    }

    public SkeletonL getSkeletonL() {
        return skeletonL;
    }
}
