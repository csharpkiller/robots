package gui;

import gui.Game.GameLogic;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class TrackerRobotWindow extends JInternalFrame implements Observer{

    public final JLabel robotPosition;
    public final JInternalFrame frame;
    private static GameLogic gameLogic;
    private static int counter;

    public TrackerRobotWindow() {
        this.gameLogic = gameLogic;
        frame = new JInternalFrame("robot position");
        robotPosition = new JLabel("position", JLabel.CENTER);
        frame.add(robotPosition);
        frame.pack();
        frame.setSize(300, 100);
        frame.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        counter++;
        if(counter > 10){
            counter=0;
            double[] pos = (double[]) arg;
            String posX = String.format("%.3f",pos[0]);
            String posY = String.format("%.3f",pos[1]);
            robotPosition.setText("X: " + posX + " Y: " + posY);
        }
    }
}