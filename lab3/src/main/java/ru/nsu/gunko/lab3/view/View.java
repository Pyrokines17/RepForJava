package ru.nsu.gunko.lab3.view;

import javafx.scene.layout.*;
import ru.nsu.gunko.lab3.model.*;
import java.util.*;

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
        switch (model.getState()) {
            case MOVE: {
                List<Integer> coordinates = model.getObj().get(id).getCoordinates();
                gameObj.get(id).move(coordinates.getFirst(), coordinates.getLast());
                break;
            }
            case ACTION: {
                List<Integer> coordinates = model.getObj().get(id).getCoordinates();
                if (model.getObj().get(id).getSide() == "right") {
                    gameObj.get(id).action(coordinates.getFirst() + 10, coordinates.getLast(), "right");
                } else {
                    gameObj.get(id).action(coordinates.getFirst() - 10, coordinates.getLast(), "left");
                }
                break;
            }
            case CHANGE_IMAGE: {
                gameObj.get(id).changeImage(model.getObj().get(id).getSide());
            }
            case DELETE_IMAGE: {
                gameObj.get(id).deleteImage();
            }
        }
    }

    private void initImages(Model model) {
        Person person;
        List<Integer> coordinates;
        for (int i = 0; i < model.getObj().size(); ++i) {
            switch (model.getObj().get(i).getName()) {
                case ("hero") : {
                    person = new HeroP(stackPane);
                    break; }
                case ("skeleton") : {
                    person = new SkeletonP(stackPane);
                    break; }
                default:
                    throw new IllegalStateException("Unexpected value: " + model.getObj().get(i).getName());
            }
            coordinates = model.getObj().get(i).getCoordinates();
            person.move(coordinates.getFirst(), coordinates.getLast());
            gameObj.add(person);
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
