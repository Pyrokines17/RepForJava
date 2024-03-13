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
        initImages();
    }

    @Override
    public void reaction() {
        switch (model.getState()) {
            case MOVE: {
                List<Integer> coordinates = model.getObj().getFirst().getCoordinates();
                gameObj.getFirst().move(coordinates.getFirst(), coordinates.getLast());
                break;
            }
            case ACTION: {}
            case CHANGE_IMAGE: {
                gameObj.getFirst().changeImage(model.getObj().getFirst().getSide());
            }
        }
    }

    private void initImages() {
        gameObj.add(new HeroP(stackPane));
        gameObj.add(new SkeletonP(stackPane));
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
