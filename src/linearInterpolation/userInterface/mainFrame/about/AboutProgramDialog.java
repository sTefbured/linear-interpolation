package linearInterpolation.userInterface.mainFrame.about;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.net.URL;

import static java.awt.Image.SCALE_FAST;

/**
 * <code>AboutProgramDialog</code> is an extension of JDialog which contains
 * information about the program including a picture that partly explains its functioning
 * and list of operations that program provides with.
 *
 * @author Kotikov S.G.
 */
public class AboutProgramDialog extends JDialog {
    private static final String TITLE = "О программе";
    private static final String PROGRAM_NAME = "Линейная интерполяция и экстраполяция";

    private final GridBagConstraints constraints;

    /**
     * Creates a dialog with added information and picture on it.
     *
     * @param parent parent <code>JFrame</code> of dialog. Used for centering.
     */
    public AboutProgramDialog(JFrame parent) {
        super(parent, TITLE);
        constraints = new GridBagConstraints();
        setLayout(new GridBagLayout());

        addProgramName();
        addPicture();
        addInfo();

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void addProgramName() {
        JLabel nameLabel = new JLabel(PROGRAM_NAME);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        nameLabel.setBorder(border);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(nameLabel, constraints);
    }

    private void addPicture() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource("icon.png");
        if (url == null) {
            return;
        }
        Image image = new ImageIcon(url).getImage();
        Image scaledImage = image.getScaledInstance(200, 200, SCALE_FAST);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        imageLabel.setBorder(border);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(imageLabel, constraints);
    }

    private void addInfo() {
        JLabel infoLabel = new JLabel("<html>Программа позволяет:<br>" +
                "1) Получать график интерполяционной прямой<br>" +
                "2) Добавлять новые точки на график<br>" +
                "2) Сохранять данные об интерполяции в файл<br>" +
                "3) Загружать сохраненные данные об интерполяции из файла</html>");
        infoLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        infoLabel.setBorder(border);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(infoLabel, constraints);
    }
}
