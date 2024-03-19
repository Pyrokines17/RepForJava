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
    private int countOfEnemy;

    public Model() {
        buildAndSetLoop();
        Random rand = new Random();
        countOfEnemy = 1 + rand.nextInt(10);
        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));
        int countOfRock = 1 + rand.nextInt(10);

        for (int i = 0; i < countOfEnemy; ++i) {
            gameObj.add(new SkeletonL(this));
        }

        for (int i = 0; i < countOfRock; ++i) {
            //ToDo: init rock's
        }
    }

    private void buildAndSetLoop() {
        Duration oneFrameAmt = Duration.millis((double) 1000 /getFPS());

        KeyFrame oneFrame = new KeyFrame(oneFrameAmt, (EventHandler) event -> {
            for (int i = 1; i < gameObj.size(); ++i) {
                gameObj.get(i).move("non");
                gameObj.get(i).action("unknown");
                gameObj.get(i).delete(countOfEnemy);
                checkEnd();
            }
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
        setGameLoop(timeline);
    }


    public void signal(int id) {
        if (modelListener != null) {
            modelListener.reaction(id);
        }
    }

    void checkEnd() {
        if (!gameObj.getFirst().equals("hero")) {
            //ToDo: fail
        }
        if (countOfEnemy == 0) {
            //ToDo: win
        }
    }

    public void start() {
        gameLoop.play();
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
}
