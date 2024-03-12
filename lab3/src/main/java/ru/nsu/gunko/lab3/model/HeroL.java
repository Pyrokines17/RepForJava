package ru.nsu.gunko.lab3.model;

import ru.nsu.gunko.lab3.view.HeroP;
import java.util.*;

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

    public void move(String side) { //maybe ToDo: remake signal's
        switch (side) {
            case "up" : {
                params.set(1, params.get(1)-10);
                params.set(2, params.get(2)==1 ? 0 : 1);
                break;}
            case "down" : {
                params.set(1, params.get(1)+10);
                params.set(2, params.get(2)==1 ? 0 : 1);
                break;}
            case "left" : {
                params.set(0, params.getFirst()-10);
                params.set(2, 1);
                break;}
            case "right" : {
                params.set(0, params.getFirst()+10);
                params.set(2, 0);
                break;}
        }

        heroP.listener(0, params);
        heroP.listener(1, params);
    }

    public void shoot(String side) {

    }
}
