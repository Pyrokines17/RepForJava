package ru.nsu.gunko.lab3.model;

import java.util.*;

public class Model {
    private ModelListener modelListener;
    private State state = State.NOTHING;
    private final List<Logic> gameObj;
    private int countOfEnemy;
    private int countOfRock;
    private int score;
    private int count;

    public Model() {
        int randCount = 10;
        Random rand = new Random();
        score = 0;
        count = 0;

        gameObj = new ArrayList<>();
        gameObj.add(new HeroL(this));
        gameObj.getFirst().setId(getObj().size()-1);

        countOfEnemy = 5;
        for (int i = 0; i < countOfEnemy; ++i) {
            gameObj.add(new SkeletonL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }

        countOfRock = 1 + rand.nextInt(randCount);
        for (int i = 0; i < countOfRock; ++i) {
            gameObj.add(new RockL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }

        int countOfHeal = 1 + rand.nextInt(randCount);
        for (int i = 0; i < countOfHeal; ++i) {
            gameObj.add(new HealL(this));
            gameObj.getLast().setId(getObj().size()-1);
        }
    }

    public void checkEnd() {
        if (!gameObj.getFirst().getName().equals("hero")) {
            System.exit(1);
        }

        if (countOfEnemy == 0) {
            System.exit(1);
        }

        if (count >= 1000 && score > 0) {
            --score;
            count = 0;
        } else {
            ++count;
        }
    }

    public void printStat() {
        setState(State.STAT);
        signal(0);
    }

    public void signal(int id) {
        if (modelListener != null) {
            modelListener.reaction(id);
        }
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

    public int getFPS() {
        return 30;
    }

    public int getCountOfEnemy() {
        return countOfEnemy;
    }

    public int getCountOfRock() {
        return countOfRock;
    }

    public int getScore() {
        return score;
    }

    public void setState(State newState) {
        state = newState;
    }

    public void setListener(ModelListener newListener) {
        modelListener = newListener;
    }

    public void removeHero() {
        gameObj.removeFirst();
    }

    public void removeEnemy() {
        countOfEnemy -= 1;
        score += 100;
    }

    public void removeRock() {
        countOfRock -= 1;
        score += 50;
    }
}
