package gui;

import gui.Game.GameVisualizer;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class TrackerRobotWindow extends JInternalFrame implements Observer{

    public JLabel robotPosition = new JLabel("position", JLabel.CENTER);

    //private JInternalFrame internalFrame = new JInternalFrame();


    public TrackerRobotWindow() {
            JFrame f = new JFrame("robot position");
            f.add(robotPosition);
            f.pack();
            f.setVisible(true);
        }
    /*public TrackerRobotWindow() {
        super("Tracker", true, true, true, true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(robotPosition);
        getContentPane().add(panel);
        pack();
    }*/


    @Override
    public void handleEvent(double[] position) {
        EventQueue.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        TrackerRobotWindow.this.robotPosition.setText("X: " + position[0] + "\n Y: " + position[1]);
                    }
                }
        );

    }
}