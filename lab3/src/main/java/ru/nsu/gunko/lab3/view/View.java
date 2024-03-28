package ru.nsu.gunko.lab3.view;

import java.util.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import ru.nsu.gunko.lab3.model.*;
import ru.nsu.gunko.lab3.controller.*;

public class View implements ModelListener {
    private final StackPane stackPane;
    private final Model model;
    private final List<Person> gameObj;
    private final Text count;
    private final Text score;
    private final Text hp;
    private final Text desc;
    private boolean flagEnd;

    public View(Model newModel) {
        count = new Text(); hp = new Text();
        score = new Text(); desc = new Text();
        gameObj = new ArrayList<>();
        stackPane = new StackPane();
        flagEnd = false;

        model = newModel;
        initImages(model);
        initText();
    }

    @Override
    public void reaction(int id) {
        int shift = 0;

        switch (model.getState()) {
            case MOVE: {
                int x = model.getObj().get(id).getX(),
                        y = model.getObj().get(id).getY();
                PlatformHelper.run(() -> gameObj.get(id).move(x, y));
                break;
            }
            case ACTION: {
                int x = model.getObj().get(id).getX(),
                        y = model.getObj().get(id).getY();
                PlatformHelper.run(() -> {
                    if (model.getObj().get(id).getSide().equals(Side.RIGHT)) {
                        gameObj.get(id).action(x+shift, y, Side.RIGHT);
                    } else {
                        gameObj.get(id).action(x-shift, y, Side.LEFT);
                    }
                });
                break;
            }
            case CHANGE_IMAGE: {
                PlatformHelper.run(() -> gameObj.get(id).changeImage(model.getObj().get(id).getSide()));
                break;
            }
            case DELETE_IMAGE: {
                PlatformHelper.run(() -> {gameObj.get(id).deleteImage(stackPane);
                    gameObj.remove(id);});
                break;
            }
            case INIT_IMAGE: {
                PlatformHelper.run(() -> gameObj.add(new BulletP(stackPane, model.getObj().get(id).getSide())));
                break;
            }
            case STAT: {
                PlatformHelper.run(() -> printStat(model.getCountOfEnemy(), model.getHero().getHp(), model.getScore()));
                break;
            }
            case INIT_TEXT: {
                PlatformHelper.run(() -> initEnd(id));
                break;
            }
            case DELETE_TEXT: stackPane.getChildren().removeLast();
        }
    }

    private void initEnd(int id) {
        String end = "Your score: " + model.getScore() + "\n" + "q/Esc -- exit";

        if (id == 0) {
            desc.setText("You lose... \n" + end);
        } else {
            desc.setText("You win! \n" + end);
        }

        if (!flagEnd) {
            stackPane.getChildren().add(desc);
            flagEnd = true;
        }
    }

    private void initImages(Model model) {
        Person person;

        for (int i = 0; i < model.getObj().size(); ++i) {
            switch (model.getObj().get(i).getName()) {
                case ("hero") : {
                    person = new HeroP(stackPane);
                    break; }
                case ("skeleton") : {
                    person = new SkeletonP(stackPane);
                    break; }
                case ("rock") : {
                    person = new RockP(stackPane);
                    break; }
                case ("heal") : {
                    person = new HealP(stackPane);
                    break; }
                default:
                    throw new IllegalStateException("Unexpected value: " + model.getObj().get(i).getName());
            }

            int x = model.getObj().get(i).getX(),
                    y = model.getObj().get(i).getY();

            person.move(x, y);
            gameObj.add(person);
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    private void initText() {
        int line1 = -450;
        int line2 = -400;
        int line3 = -350;
        int line4 = 0;

        initStr(hp, line1);
        initStr(count, line2);
        initStr(score, line3);
        initStr(desc, line4);

        desc.setFill(Color.AQUA);
        desc.setText("""
                Begin of game -- 'Enter'\s
                [w,a,s,d] -- move\s
                [j,k] -- shoot\s
                q/Esc -- exit\s
                """);

        stackPane.getChildren().add(count);
        stackPane.getChildren().add(hp);
        stackPane.getChildren().add(score);
        stackPane.getChildren().add(desc);
    }

    private void initStr(Text str, int line) {
        str.setFill(Color.BROWN);
        str.setTranslateX(0);
        str.setTranslateY(line);
        str.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 55));
    }

    public void printStat(int countOfEnemy, int countOfHP, int countOfScore) {
        count.setText("Enemy:" + countOfEnemy);
        hp.setText("HP:" + countOfHP);
        score.setText("Score:" + countOfScore);
    }
}
