package ru.nsu.gunko.lab3.view;

import java.util.*;
import javafx.scene.layout.*;
import ru.nsu.gunko.lab3.model.*;
import ru.nsu.gunko.lab3.controller.*;

public class View implements ModelListener {
    private final StackPane stackPane;
    private final Model model;
    private final List<Person> gameObj;

    public View(Model newModel) {
        gameObj = new ArrayList<>();
        stackPane = new StackPane();
        model = newModel;
        initImages(model);
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
}
