package ru.nsu.gunko.lab3.controller;

import java.util.List;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import ru.nsu.gunko.lab3.model.*;

public class Controller {
    private Timeline gameLoop;
    private final Model model;

    public Controller(Model newModel) {
        model = newModel;
        buildAndSetLoop();
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

    private void buildAndSetLoop() {
        Duration oneFrameAmt = Duration.millis((double) 1000 /model.getFPS());
        List<Logic> gameObj =  model.getObj();

        KeyFrame oneFrame = new KeyFrame(oneFrameAmt, event -> {
            for (int i = 0; i < gameObj.size(); ++i) {
                if (i != 0) {
                    gameObj.get(i).move("non");
                    gameObj.get(i).action("unknown");
                }

                if (gameObj.get(i).delete()) {
                    for (int j = i; j < gameObj.size(); ++j) {
                        gameObj.get(j).setId(j);
                    }

                    i -= 1;
                }

                model.checkEnd();
            }
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
        setGameLoop(timeline);
    }

    public void setGameLoop(Timeline newGameLoop) {
        gameLoop = newGameLoop;
    }

    public void start() {
        gameLoop.play();
    }
}
