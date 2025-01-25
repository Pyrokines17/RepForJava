package ru.nsu.gunko.lab3.controller;

import java.io.*;
import java.util.*;

import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

import ru.nsu.gunko.lab3.model.*;

import javax.sound.sampled.*;

public class Controller {
    private Timeline gameLoop;
    private final Model model;
    private boolean flagBeg;

    public Controller(Model newModel) {
        model = newModel;
        flagBeg = true;
        buildAndSetLoop();
    }

    public void entry(KeyCode code) {
        if (code == KeyCode.ESCAPE || code == KeyCode.Q) {
            System.exit(0);
        }

        if (code == KeyCode.ENTER && flagBeg) {
            model.setState(State.DELETE_TEXT);
            model.signal(0);
            flagBeg = false;
            start();
        }

        if (!flagBeg) {
            switch (code) {
                case KeyCode.W: {
                    model.getHero().move("up");
                    break;
                }
                case KeyCode.S: {
                    model.getHero().move("down");
                    break;
                }
                case KeyCode.A: {
                    model.getHero().move("left");
                    break;
                }
                case KeyCode.D: {
                    model.getHero().move("right");
                    break;
                }

                case KeyCode.J: {
                    model.getHero().action("left");
                    break;
                }
                case KeyCode.K: {
                    model.getHero().action("right");
                    break;
                }
            }
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

                if (model.checkEnd()) {
                    gameLoop.stop();
                }

                model.printStat();
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

    public void playMusic() {
        try {
            File soundFile = new File(Objects.requireNonNull(getClass().getResource("/ru/nsu/gunko/lab3/controller/sound.wav")).getPath());
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);

            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.getLocalizedMessage();
        }
    }
}
