package gui;

import gui.Ser.GameWindowSer;
import gui.Ser.LogWindowSer;
import gui.Ser.MainFrameSer;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
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

        setJMenuBar(generateMenuBar(logWindow, gameWindow));
        //setDefaultCloseOperation(EXIT_ON_CLOSE); def


        setTitle("Main Application");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e){
                try {
                    MainFrameSer mainFrameSer = (MainFrameSer) restoreState("mainFrame.json");
                    setBounds(mainFrameSer.getX(),
                            mainFrameSer.getY(),
                            mainFrameSer.getWidth(),
                            mainFrameSer.getHeight());

                    setState(mainFrameSer.getState());
                    LogWindowSer logWindowSer = (LogWindowSer) restoreState("logWindow.json");

                    logWindow.setLocation(logWindowSer.getX(), logWindowSer.getY());
                    logWindow.setSize(logWindowSer.getWidth(), logWindowSer.getHeight());
                    try {
                        logWindow.setIcon(logWindowSer.isIcon());
                        logWindow.setSelected(logWindowSer.isSelected());
                    } catch (PropertyVetoException ex) {
                        throw new RuntimeException(ex);
                    }

                    GameWindowSer gameWindowSer = (GameWindowSer) restoreState("gameWindow.json");
                    gameWindow.setLocation(gameWindowSer.getX(), gameWindowSer.getY());
                    gameWindow.setSize(gameWindowSer.getWidth(), gameWindowSer.getHeight());
                    try {
                        gameWindow.setIcon(gameWindowSer.isIcon());
                        gameWindow.setSelected(gameWindowSer.isSelected());
                    } catch (PropertyVetoException ex) {
                        throw new RuntimeException(ex);
                    }


                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    MainFrameSer mainFrameSer = new MainFrameSer(getX(),getY(),getWidth(),getHeight(),getState());
                    saveState("mainFrame.json", mainFrameSer);
                    LogWindowSer logWindowSer = new LogWindowSer(logWindow.getX(),
                            logWindow.getY(),
                            logWindow.getWidth(),
                            logWindow.getHeight(),
                            logWindow.isIcon(),
                            logWindow.isSelected());
                    saveState("logWindow.json", logWindowSer);
                    GameWindowSer gameWindowSer = new GameWindowSer(gameWindow.getX(),
                            gameWindow.getY(),
                            gameWindow.getWidth(),
                            gameWindow.getHeight(),
                            gameWindow.isIcon(),
                            gameWindow.isSelected());
                    saveState("gameWindow.json", gameWindowSer);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar(LogWindow logWindow, GameWindow gameWindow)
    {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        JMenuBar menuBar = new JMenuBar();


        /*JMenu exit = new JMenu("Выход");
        ExitAction exitAction = new ExitAction();
        exit.add(exitAction);*/

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }
    // secv pizda lagaet nice nout

    private JMenu createExitMenu(){

        JMenu exitMenu = new JMenu("Выйти из приложения");
        exitMenu.setMnemonic(KeyEvent.VK_Q);
        {
            JMenuItem exitMenuItem = new JMenuItem("Выйти", KeyEvent.VK_Q);
            exitMenuItem.addActionListener(e -> {
                int input = JOptionPane.showConfirmDialog(null,
                        "Вы точно хотите выйти?", "Подтверждение выхода", JOptionPane.YES_NO_OPTION);
                if (input == 0)
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            });
            exitMenu.add(exitMenuItem);
        }
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

    private void saveState(String fileName, Object objectToSave) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(objectToSave);
        oos.flush();
        oos.close();
        fos.close();
    }

    private Object restoreState(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object res = objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return res;
    }
}
