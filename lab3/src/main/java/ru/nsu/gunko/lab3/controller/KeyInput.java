package ru.nsu.gunko.lab3.controller;

import javafx.scene.input.KeyCode;
import ru.nsu.gunko.lab3.model.Context;

public class KeyInput {
    private final Context context;

    public KeyInput(Context newContext) {
        context = newContext;
    }

    public void entry(KeyCode code) {
        if (code == KeyCode.ESCAPE || code == KeyCode.Q) {
            System.exit(0);
        }

        switch (code) {
            case KeyCode.W : {context.getHero().move("up"); break;}
            case KeyCode.S : {context.getHero().move("down"); break;}
            case KeyCode.A : {context.getHero().move("left"); break;}
            case KeyCode.D : {context.getHero().move("right"); break;}

            case KeyCode.UP: {context.getHero().shoot("up"); break;}
            case KeyCode.DOWN: {context.getHero().shoot("down"); break;}
            case KeyCode.LEFT: {context.getHero().shoot("left"); break;}
            case KeyCode.RIGHT: {context.getHero().shoot("right"); break;}
        }
    }
}
