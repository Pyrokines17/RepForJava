package ru.nsu.gunko.lab3.controllerSwing;

import ru.nsu.gunko.lab3.model.*;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.Timer;
import java.awt.event.*;

public class ControllerSwing {
    private Timer gameLoop;
    private final Model model;
    private boolean flagBeg;

    public ControllerSwing(Model newModel) {
        model = newModel;
        flagBeg = true;
        buildAndSetLoop();
    }

    public void entry(KeyEvent code) {
        if (code.getKeyCode() == KeyEvent.VK_Q || code.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        if (code.getKeyCode() == KeyEvent.VK_ENTER && flagBeg) {
            model.setState(State.DELETE_TEXT);
            model.signal(0);
            flagBeg = false;
            start();
        }

        if (!flagBeg) {
            switch (code.getKeyCode()) {
                case KeyEvent.VK_W: {
                    model.getHero().move("up");
                    break;
                }
                case KeyEvent.VK_S: {
                    model.getHero().move("down");
                    break;
                }
                case KeyEvent.VK_A: {
                    model.getHero().move("left");
                    break;
                }
                case KeyEvent.VK_D: {
                    model.getHero().move("right");
                    break;
                }

                case KeyEvent.VK_J: {
                    model.getHero().action("left");
                    break;
                }
                case KeyEvent.VK_K: {
                    model.getHero().action("right");
                    break;
                }
            }
        }
    }

    private void buildAndSetLoop() {
        List<Logic> gameObj =  model.getObj();

        ActionListener oneFrame = evt -> {
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
        };

        Timer timeline = new Timer(100, oneFrame);
        setGameLoop(timeline);
    }

    public void setGameLoop(javax.swing.Timer newGameLoop) {
        gameLoop = newGameLoop;
    }

    public void start() {
        gameLoop.start();
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