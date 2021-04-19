package linearInterpolation.userInterface;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.LinearInterpolation;
import linearInterpolation.userInterface.panels.GraphPanel;
import linearInterpolation.userInterface.panels.InitialValuesPanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class MainFrame extends JFrame {
    private Interpolation interpolation;
    private GraphPanel graphPanel;
    private InitialValuesPanel initialValuesPanel;
    private final JFileChooser fileChooser;

    public MainFrame() {
        super("Linear interpolation coursework");
        fileChooser = new JFileChooser();
        fileChooser.setSize(300, 200);
        FileFilter filter = new FileNameExtensionFilter(
                "Java serialization file .ser", "ser");
        fileChooser.setFileFilter(filter);

        setLayout(new GridLayout(1, 2));
        addComponents();
        setJMenuBar(createMenuBar());
        setSize(800, 500);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addComponents() {
        interpolation = new LinearInterpolation();
        initialValuesPanel = new InitialValuesPanel(interpolation);
        add(initialValuesPanel);
        graphPanel = new GraphPanel(interpolation);
        add(graphPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
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

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem saveItem = new JMenuItem("Save");
        loadItem.addActionListener(e -> load());
        saveItem.addActionListener(e -> save());
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        return fileMenu;
    }

    private void load() {
        fileChooser.setSelectedFile(null);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = fileChooser.getSelectedFile();
        try (FileInputStream fileStream = new FileInputStream(file);
             ObjectInputStream inputStream = new ObjectInputStream(fileStream)) {
            updateInterpolation((Interpolation) inputStream.readObject());
        } catch (IOException | ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error. Wrong file format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
        graphPanel.setInterpolation(interpolation);
        initialValuesPanel.setInterpolation(interpolation);
    }

    private void save() {
        fileChooser.setSelectedFile(new File("interpolation1.ser"));
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        try (FileOutputStream file = new FileOutputStream(selectedFile);
             ObjectOutputStream input = new ObjectOutputStream(file)) {
            input.writeObject(interpolation);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error. Couldn't save file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
