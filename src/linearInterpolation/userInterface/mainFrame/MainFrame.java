package linearInterpolation.userInterface.mainFrame;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.LinearInterpolation;
import linearInterpolation.userInterface.mainFrame.chart.InterpolationChartPanel;
import linearInterpolation.userInterface.mainFrame.userInput.InputPanel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class MainFrame extends JFrame {
    private Interpolation interpolation;
    private InterpolationChartPanel chartPanel;
    private InputPanel inputPanel;
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
        Dimension screenSize = getToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addComponents() {
        interpolation = new LinearInterpolation();
        inputPanel = new InputPanel(interpolation);
        add(inputPanel);
        chartPanel = new InterpolationChartPanel(interpolation);
        add(chartPanel);
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
        loadItem.addActionListener(e -> loadInterpolation());
        saveItem.addActionListener(e -> saveInterpolation());
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        return fileMenu;
    }

    private void loadInterpolation() {
        fileChooser.setSelectedFile(null);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        try (FileInputStream file = new FileInputStream(selectedFile);
                ObjectInputStream inStream = new ObjectInputStream(file)) {
            updateInterpolation((Interpolation) inStream.readObject());
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
        chartPanel.setInterpolation(interpolation);
        inputPanel.setInterpolation(interpolation);
    }

    private void saveInterpolation() {
        fileChooser.setSelectedFile(new File("interpolation1.ser"));
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        try (FileOutputStream file = new FileOutputStream(selectedFile);
                ObjectOutputStream outStream = new ObjectOutputStream(file)) {
            outStream.writeObject(interpolation);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error. Couldn't save file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
