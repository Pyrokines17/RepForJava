package ru.nsu.gunko.lab3.controller;

import javafx.scene.input.KeyCode;
import ru.nsu.gunko.lab3.model.Model;

public class Controller {
    private final Model model;

    public Controller(Model newModel) {
        model = newModel;
    }

    public void entry(KeyCode code) {
        if (code == KeyCode.ESCAPE || code == KeyCode.Q) {
            System.exit(0);
        }

        switch (code) {
            case KeyCode.W : {
                model.getHero().move("up"); break;}
            case KeyCode.S : {
                model.getHero().move("down"); break;}
            case KeyCode.A : {
                model.getHero().move("left"); break;}
            case KeyCode.D : {
                model.getHero().move("right"); break;}

            case KeyCode.J: {
                model.getHero().action("left"); break;}
            case KeyCode.K: {
                model.getHero().action("right"); break;}
        }
    }
}
