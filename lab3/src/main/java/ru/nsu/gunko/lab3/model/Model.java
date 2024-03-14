package ru.nsu.gunko.lab3.model;

import javafx.animation.*;
import javafx.event.*;
import javafx.util.Duration;
import java.util.*;

public class Model {
    private Timeline gameLoop;
    private ModelListener modelListener;
    private State state = State.NOTHING;
    private final List<Logic> gameObj;

    public Model() {
        buildAndSetLoop();
        Random rand = new Random();
        int countEnemy = rand.nextInt(6);
        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));

        for (int i = 0; i < countEnemy; ++i) {
            gameObj.add(new SkeletonL(this));
        }
    }

    private void buildAndSetLoop() {
        Duration oneFrameAmt = Duration.millis((double) 1000 /getFPS());

        KeyFrame oneFrame = new KeyFrame(oneFrameAmt, (EventHandler) event -> {
            for (int i = 1; i < gameObj.size(); ++i) {
                gameObj.get(i).move("non");
                gameObj.get(i).action("unknown");
            }
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
        setGameLoop(timeline);
    }

    public List<Logic> getObj() {
        return gameObj;
    }

    public State getState() {
        return state;
    }

    private int getFPS() {
        return 30;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void setListener(ModelListener newListener) {
        modelListener = newListener;
    }

    public void setGameLoop(Timeline newGameLoop) {
        gameLoop = newGameLoop;
    }

    public void signal(int id) {
        if (modelListener != null) {
            modelListener.reaction(id);
        }
    }

    public void start() {
        gameLoop.play();
    }
}
