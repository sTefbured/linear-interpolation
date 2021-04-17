package linearInterpolation.userInterface;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.LinearInterpolation;
import linearInterpolation.userInterface.panels.GraphPanel;
import linearInterpolation.userInterface.panels.InitialValuesPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Linear interpolation coursework");
        addComponents();
        setJMenuBar(createMenuBar());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // TODO: think about different naming
    private void addComponents() {
        setLayout(new GridLayout(1, 2));
        Interpolation interpolation = new LinearInterpolation();
        add(new InitialValuesPanel(interpolation));
        add(new GraphPanel(interpolation));
    }

    // TODO: add more menu items
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createAboutMenu());
        return menuBar;
    }

    // TODO: fill listeners
    private JMenu createAboutMenu() {
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutDeveloperItem = new JMenuItem("Developer");
        JMenuItem aboutProgramItem = new JMenuItem("Program");
        aboutDeveloperItem.addActionListener(e -> {

        });
        aboutProgramItem.addActionListener(e -> {

        });
        aboutMenu.add(aboutDeveloperItem);
        aboutMenu.add(aboutProgramItem);
        return aboutMenu;
    }
}
