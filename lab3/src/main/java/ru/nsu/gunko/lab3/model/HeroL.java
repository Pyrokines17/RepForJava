package ru.nsu.gunko.lab3.model;

import ru.nsu.gunko.lab3.view.HeroP;
import java.util.ArrayList;
import java.util.List;

public class HeroL implements Logic {
    private final HeroP heroP;
    private final List<Integer> params;

    public HeroL(HeroP newHeroP) {
        heroP = newHeroP;
        params = new ArrayList<>();
        params.add(0); params.add(0);
        params.add(0);
        //x, y, side//
    }

    public void move(String side) {
        switch (side) {
            case "up" : {params.set(1, params.get(1)-10); break;}
            case "down" : {params.set(1, params.get(1)+10); break;}
            case "left" : {params.set(0, params.getFirst()-10); break;}
            case "right" : {params.set(0, params.getFirst()+10); break;}
        }

        heroP.listener(0, params);
    }

    public void shoot(String side) {

    }
}
