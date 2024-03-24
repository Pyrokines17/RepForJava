package ru.nsu.gunko.lab3.view;

import java.util.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.nsu.gunko.lab3.model.*;
import ru.nsu.gunko.lab3.controller.*;

public class View implements ModelListener {
    private final StackPane stackPane;
    private final Model model;
    private final List<Person> gameObj;
    private final Text count;
    private final Text score;
    private final Text hp;

    public View(Model newModel) {
        count = new Text(); hp = new Text(); score = new Text();
        gameObj = new ArrayList<>();
        stackPane = new StackPane();

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
        count.setFill(Color.BROWN);
        score.setFill(Color.BROWN);
        hp.setFill(Color.BROWN);

        int line1 = -450;
        int line2 = -400;
        int line3 = -350;

        count.setTranslateX(0);
        count.setTranslateY(line2);
        count.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 55));

        hp.setTranslateX(0);
        hp.setTranslateY(line1);
        hp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 55));

        score.setTranslateX(0);
        score.setTranslateY(line3);
        score.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 55));

        stackPane.getChildren().add(count);
        stackPane.getChildren().add(hp);
        stackPane.getChildren().add(score);
    }

    public void printStat(int countOfEnemy, int countOfHP, int countOfScore) {
        count.setText("Enemy:" + countOfEnemy);
        hp.setText("HP:" + countOfHP);
        score.setText("Score:" + countOfScore);
    }
}
