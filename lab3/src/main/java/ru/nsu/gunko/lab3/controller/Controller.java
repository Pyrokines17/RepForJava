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
                model.getObj().getFirst().move("up"); break;}
            case KeyCode.S : {
                model.getObj().getFirst().move("down"); break;}
            case KeyCode.A : {
                model.getObj().getFirst().move("left"); break;}
            case KeyCode.D : {
                model.getObj().getFirst().move("right"); break;}

            case KeyCode.UP: {
                model.getObj().getFirst().action("up"); break;}
            case KeyCode.DOWN: {
                model.getObj().getFirst().action("down"); break;}
            case KeyCode.LEFT: {
                model.getObj().getFirst().action("left"); break;}
            case KeyCode.RIGHT: {
                model.getObj().getFirst().action("right"); break;}
        }
    }
}
