package gui;

import gui.Game.GameLogic;
import gui.Ser.*;
import log.Logger;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */

//System.getProperty();
//gameWindow.isMaximum();
//gameWindow.getNormalBounds(); ???
public class MainApplicationFrame extends JFrame
{
    private static final long serialVersionUID = 1L;
    private transient final JDesktopPane desktopPane = new JDesktopPane();
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        GameLogic gameLogic = gameWindow.getM_visualizer().gameLogic;
        TrackerRobotWindow trackerRobotWindow = new TrackerRobotWindow();

        gameLogic.addObserver(trackerRobotWindow);

        trackerRobotWindow.setSize(400,  400);
        addWindow(trackerRobotWindow.frame);

        setJMenuBar(generateMenuBar(logWindow, gameWindow));

        setTitle("Main Application");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e){
                ArrayList framePositions = (ArrayList) restoreState("framePositions.json");

                Map positionMap = (HashMap)framePositions.get(0);
                String numberToFrameStr = positionMap.get("MainWindow").toString();

                ArrayList<JFrame> arrayList = new ArrayList<>();

                HashMap mainFramePosition = (HashMap) framePositions.get(Integer.parseInt(numberToFrameStr));
                setBounds(Integer.parseInt(mainFramePosition.get("X").toString()),
                        Integer.parseInt(mainFramePosition.get("Y").toString()),
                        Integer.parseInt(mainFramePosition.get("Width").toString()),
                        Integer.parseInt(mainFramePosition.get("Height").toString()));

                setWindowPosition(logWindow, positionMap, framePositions, "LogWindow");
                setWindowPosition(gameWindow, positionMap, framePositions, "GameWindow");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Map<String, String> mainFramePosition = SaveFramePosition.savePosition(getComponent(0).getParent()); // как - то поменять
                Map<String, String> logWindowPosition = SaveFramePosition.savePosition(logWindow);
                Map<String, String> gameWindowPosition = SaveFramePosition.savePosition(gameWindow);

                Map<String, String> alias = new HashMap<>();
                alias.put("MainWindow", "1");
                alias.put("LogWindow", "2");
                alias.put("GameWindow", "3");

                ArrayList<Map<String, String>> mapArrayListToSave = new ArrayList<>();
                mapArrayListToSave.add(alias);
                mapArrayListToSave.add(mainFramePosition);
                mapArrayListToSave.add(logWindowPosition);
                mapArrayListToSave.add(gameWindowPosition);

                saveState("framePositions.json", mapArrayListToSave);
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);


    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    private JMenuBar generateMenuBar(LogWindow logWindow, GameWindow gameWindow)
    {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }
    private JMenu createExitMenu(){

        JMenu exitMenu = new JMenu("Выйти из приложения");
        exitMenu.setMnemonic(KeyEvent.VK_Q);

            JMenuItem exitMenuItem = new JMenuItem("Выйти", KeyEvent.VK_Q);
            exitMenuItem.addActionListener(e -> {
                int input = JOptionPane.showConfirmDialog(null,
                        "Вы точно хотите выйти?", "Подтверждение выхода", JOptionPane.YES_NO_OPTION);
                if (input == 0)
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            });
            exitMenu.add(exitMenuItem);

        return exitMenu;
    }

    private JMenu createLookAndFeelMenu(){
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu(){
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }


    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private void saveState(String fileName, Object objectToSave){
        Properties properties = System.getProperties();
        String userHome = properties.getProperty("user.home");
        var targetPath = Path.of(userHome).resolve(fileName);
        try(FileOutputStream fos = new FileOutputStream(targetPath.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(objectToSave);
            oos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Object restoreState(String fileName){
        Properties properties = System.getProperties();
        String userHome = properties.getProperty("user.home");
        var targetPath = Path.of(userHome).resolve(fileName);
        Object res = null;
        try(FileInputStream fileInputStream = new FileInputStream(targetPath.toFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){
            res = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public static void setWindowPosition(JInternalFrame frame,
                                  Map<String, String> alias,
                                  ArrayList<Map<String, String>> windowsPositions,
                                  String windowName){

        int listNumber = Integer.parseInt(alias.get(windowName));
        Map windowPosition = windowsPositions.get(listNumber);

        switch (windowName){
            case "LogWindow":
                frame = (LogWindow)frame;
                break;
            case "GameWindow":
                frame = (GameWindow)frame;
                break;
        }

        frame.setLocation(Integer.parseInt(windowPosition.get("X").toString()), Integer.parseInt(windowPosition.get("Y").toString()));
        frame.setSize(Integer.parseInt(windowPosition.get("Width").toString()), Integer.parseInt(windowPosition.get("Height").toString()));
        try {
            frame.setIcon(Boolean.parseBoolean(windowPosition.get("IsIcon").toString()));
            frame.setSelected(Boolean.parseBoolean(windowPosition.get("IsSelected").toString()));
            frame.setMaximum(Boolean.parseBoolean(windowPosition.get("IsMaximum").toString()));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

}
