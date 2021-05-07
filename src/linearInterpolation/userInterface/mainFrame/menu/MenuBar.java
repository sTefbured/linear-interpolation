package linearInterpolation.userInterface.mainFrame.menu;

import linearInterpolation.model.interpolation.Interpolation;
import linearInterpolation.userInterface.mainFrame.MainFrame;
import linearInterpolation.userInterface.mainFrame.about.AboutAuthorDialog;
import linearInterpolation.userInterface.mainFrame.about.AboutProgramDialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * MenuBar is an extension of JMenuBar which contains menu items
 * for saving/loading interpolation data files (.intp) and about section.
 * About section consists of "About author" and "About program" items.
 * "About" items open "About" dialogs with certain information.
 *
 * @author Kotikov S.G.
 */
public class MenuBar extends JMenuBar {
    private final String fileExtension = "intp";
    private final String fileDescription = "Interpolation data file .intp";

    private final MainFrame parentFrame;
    private final JFileChooser fileChooser;

    /**
     * Creates <code>MenuBar</code> object with "Load", "Save" and "About" items.
     * Initializes <code>parentFrame</code> which used for centering dialogs relatively to parent frame.
     *
     * @param parent parent frame of the menu bar.
     */
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
        if (MainFrame.getInterpolation().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                    "You can not save empty interpolation.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
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

    private JMenu createAboutMenu() {
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutDeveloperItem = new JMenuItem("Developer");
        JMenuItem aboutProgramItem = new JMenuItem("Program");
        aboutDeveloperItem.addActionListener(e -> new AboutAuthorDialog(parentFrame));
        aboutProgramItem.addActionListener(e -> new AboutProgramDialog(parentFrame));
        aboutMenu.add(aboutDeveloperItem);
        aboutMenu.add(aboutProgramItem);
        return aboutMenu;
    }
}
