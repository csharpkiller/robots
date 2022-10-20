package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/*
public class ExitAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;
    ExitAction() {
        putValue(NAME, "Выход");
    }
    public void actionPerformed(ActionEvent e) {
        //int input = JOptionPane.showConfirmDialog(null, "Ты хочешь выйти?");
        int input = JOptionPane.showConfirmDialog(null,
                "Вы точно хотите выйти?", "Подтверждение выхода",JOptionPane.YES_NO_OPTION);
        if(input == 0) System.exit(0);
    }
}*/

public class ExitAction implements ActionListener{
   /* private GameWindow gameWindow;
    private LogWindow logWindow;*/

    @Override
    public void actionPerformed(ActionEvent e) {
         /*Point gameWindowLocation = gameWindow.getLocation();
         Point logWindowLocation = logWindow.getLocation();*/
        closeMenu();
    }

    /*public void getWindows(LogWindow logWindow, GameWindow gameWindow){
        this.logWindow = logWindow;
        this.gameWindow = gameWindow;
    }*/
    private void closeMenu(){
        int input = JOptionPane.showConfirmDialog(null,
                "Вы точно хотите выйти?", "Подтверждение выхода",JOptionPane.YES_NO_OPTION);
        if(input == 0) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
}