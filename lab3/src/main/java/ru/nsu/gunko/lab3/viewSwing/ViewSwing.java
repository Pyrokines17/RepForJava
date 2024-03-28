package ru.nsu.gunko.lab3.viewSwing;

import ru.nsu.gunko.lab3.controllerSwing.PlatformHelperSwing;
import ru.nsu.gunko.lab3.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewSwing extends JFrame implements ModelListener {
    private final JLayeredPane stackPane;
    private final Model model;
    private final List<PersonSwing> gameObj;

    private final JLabel count;
    private final JLabel score;
    private final JLabel hp;
    private final JLabel desc;

    public ViewSwing(Model newModel) {
        super("New Game!");
        setPreferredSize(new Dimension(1640, 1010));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        model = newModel;

        count = new JLabel(); hp = new JLabel();
        score = new JLabel(); desc = new JLabel();
        gameObj = new ArrayList<>(); stackPane = new JLayeredPane();

        initImages(model); initText();
        this.setContentPane(stackPane);
        pack();

        setLocationRelativeTo(null);
        model.setListener(this);
    }

    @Override
    public void reaction(int id) {
        switch (model.getState()) {
            case MOVE: {
                int x = model.getObj().get(id).getX(),
                        y = model.getObj().get(id).getY();
                PlatformHelperSwing.run(() -> {gameObj.get(id).moveP(x, y);
                    repaint();});
                break;
            }
            case DELETE_IMAGE: {
                PlatformHelperSwing.run(() -> {gameObj.get(id).deleteImage(stackPane);
                    gameObj.remove(id);
                    repaint();});
                break;
            }
            case INIT_IMAGE: {
                PlatformHelperSwing.run(() -> {gameObj.add(new BulletPSwing(stackPane, model.getObj().get(id).getSide(), this.getPreferredSize()));
                    repaint();});
                break;
            }
            case STAT: {
                PlatformHelperSwing.run(() -> {printStat(model.getCountOfEnemy(), model.getHero().getHp(), model.getScore());
                    repaint();});
                break;
            }
            case INIT_TEXT: {
                PlatformHelperSwing.run(() -> initEnd(id));
                break;
            }
            case DELETE_TEXT: {
                PlatformHelperSwing.run(() -> desc.setVisible(false));
                break;
            }
        }
    }

    private void initEnd(int id) {
        String end = "Your score: " + model.getScore() + "<br/>" + "q/Esc -- exit";

        if (id == 0) {
            desc.setText("<html>You lose... <br/>" + end + "</html>");
        } else {
            desc.setText("<html>You win! <br/>" + end + "</html>");
        }

        desc.setVisible(true);
    }

    private void initImages(Model model) {
        PersonSwing personSwing;

        for (int i = 0; i < model.getObj().size(); ++i) {
            switch (model.getObj().get(i).getName()) {
                case ("hero") : {
                    personSwing = new HeroPSwing(stackPane, this.getPreferredSize());
                    break; }
                case ("skeleton") : {
                    personSwing = new SkeletonPSwing(stackPane, this.getPreferredSize());
                    break; }
                case ("rock") : {
                    personSwing = new RockPSwing(stackPane, this.getPreferredSize());
                    break; }
                case ("heal") : {
                    personSwing = new HealPSwing(stackPane, this.getPreferredSize());
                    break; }
                default:
                    throw new IllegalStateException("Unexpected value: " + model.getObj().get(i).getName());
            }

            int x = model.getObj().get(i).getX(),
                    y = model.getObj().get(i).getY();

            personSwing.moveP(x, y);
            gameObj.add(personSwing);
        }
    }

    public JLayeredPane getStackPane() {
        return stackPane;
    }

    private void initText() {
        initStr(count);
        initStr(hp);
        initStr(score);
        initStr(desc);

        int line1 = -450, line2 = -400,
                line3 = -350, line4 = 0;

        desc.setText("""
                <html>
                Begin of game -- 'Enter'<br/>
                [w,a,s,d] -- move<br/>
                [j,k] -- shoot<br/>
                q/Esc -- exit<br/>
                </html>
                """);

        int shift = 100;
        int base = getPreferredSize().width / 2;
        count.setLocation(base-shift, line1);
        hp.setLocation(base-shift, line2);
        score.setLocation(base-shift, line3);
        desc.setLocation(base-shift, line4);
    }

    private void initStr(JLabel str) {
        str.setFont(new Font("Times New Roman", Font.PLAIN, 55));
        str.setSize(getPreferredSize());
        str.setForeground(Color.RED);
        stackPane.add(str);
        stackPane.setLayer(str, stackPane.highestLayer()+1);
    }

    public void printStat(int countOfEnemy, int countOfHP, int countOfScore) {
        count.setText("Enemy:" + countOfEnemy);
        hp.setText("HP:" + countOfHP);
        score.setText("Score:" + countOfScore);
    }
}
