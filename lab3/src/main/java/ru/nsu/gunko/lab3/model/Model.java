package ru.nsu.gunko.lab3.model;

//ToDo: remove javafx

import javafx.animation.*;
import javafx.event.*;
import javafx.util.*;

import java.util.*;

public class Model {
    private Timeline gameLoop;
    private ModelListener modelListener;
    private State state = State.NOTHING;
    private final List<Logic> gameObj;
    private int countOfEnemy;

    public Model() {
        int randCount = 10;
        buildAndSetLoop();
        Random rand = new Random();

        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));
        gameObj.getFirst().setId(getObj().size()-1);

        countOfEnemy = 1;
        for (int i = 0; i < countOfEnemy; ++i) {
            gameObj.add(new SkeletonL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }

        int countOfHeal = 1 + rand.nextInt(randCount);
        for (int i = 0; i < countOfHeal; ++i) {
            gameObj.add(new HealL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }

        int countOfRock = 1 + rand.nextInt(randCount);
        for (int i = 0; i < countOfRock; ++i) {
            gameObj.add(new RockL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }
    }

    private void buildAndSetLoop() {
        Duration oneFrameAmt = Duration.millis((double) 1000 /getFPS());

        KeyFrame oneFrame = new KeyFrame(oneFrameAmt, (EventHandler) event -> {
            for (int i = 0; i < gameObj.size(); ++i) {
                if (i != 0) {
                    gameObj.get(i).move("non");
                    gameObj.get(i).action("unknown");
                }
                if (gameObj.get(i).delete() == 1) {
                    for (int j = i; j < gameObj.size(); ++j) {
                        gameObj.get(j).setId(j);
                    }
                }
                checkEnd();
            }
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(oneFrame);
        setGameLoop(timeline);
    }

    private void checkEnd() {
        if (!gameObj.getFirst().getName().equals("hero")) {
            System.exit(1);
        }
        if (countOfEnemy == 0) {
            System.exit(1);
        }
    }

    public void signal(int id) {
        if (modelListener != null) {
            modelListener.reaction(id);
        }
    }

    public void start() {
        gameLoop.play();
    }

    public Logic getHero() {
        return gameObj.getFirst();
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

    public void removeHero() {
        gameObj.removeFirst();
    }

    public void removeEnemy() {
        countOfEnemy -= 1;
    }
}
