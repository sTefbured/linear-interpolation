package linearInterpolation.userInterface.mainFrame.menu;

import linearInterpolation.model.Interpolation;
import linearInterpolation.userInterface.mainFrame.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class MenuBar extends JMenuBar {
    private final String fileExtension = "intp";
    private final String fileDescription = "Interpolation data file .intp";

    private final MainFrame parentFrame;
    private final JFileChooser fileChooser;

    public MenuBar(MainFrame parent) {
        super();
        parentFrame = parent;
        fileChooser = createFileChooser();
        add(createFileMenu());
        add(createAboutMenu());
    }

    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSize(300, 200);
        FileFilter filter = new FileNameExtensionFilter(fileDescription, fileExtension);
        fileChooser.setFileFilter(filter);
        fileChooser.addActionListener(e -> {
            if (fileChooser.getDialogType() != JFileChooser.SAVE_DIALOG) {
                return;
            }
            File file = fileChooser.getSelectedFile();

            // If file name does not end with correct extension, add it
            if (!file.getName().endsWith('.' + fileExtension)) {
                String name = file.getAbsolutePath() + '.' + fileExtension;
                fileChooser.setSelectedFile(new File(name));
            }
        });
        return fileChooser;
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
        int result = fileChooser.showOpenDialog(parentFrame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        try (FileInputStream file = new FileInputStream(selectedFile);
             ObjectInputStream inStream = new ObjectInputStream(file)) {
            updateInterpolation((Interpolation) inStream.readObject());
        } catch (IOException | ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Error. Wrong file format. Necessary format: "
                            + fileDescription,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInterpolation(Interpolation interpolation) {
        MainFrame.setInterpolation(interpolation);
    }

    private void saveInterpolation() {
        File initialFile = new File("interpolation1." + fileExtension);
        fileChooser.setSelectedFile(initialFile);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int result = fileChooser.showSaveDialog(parentFrame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = fileChooser.getSelectedFile();
        try (FileOutputStream file = new FileOutputStream(selectedFile);
             ObjectOutputStream outStream = new ObjectOutputStream(file)) {
            outStream.writeObject(MainFrame.getInterpolation());
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Error. Couldn't save file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
