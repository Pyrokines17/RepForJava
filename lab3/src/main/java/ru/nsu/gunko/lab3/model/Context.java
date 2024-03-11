package ru.nsu.gunko.lab3.model;

import ru.nsu.gunko.lab3.view.Window;

public class Context {
    private final Window window;
    private final HeroL heroL;

    public Context(Window newWindow) {
        window = newWindow;
        heroL = new HeroL(window.getHeroP());
    }

    public HeroL getHero() {
        return heroL;
    }
}
