package linearInterpolation.userInterface.mainFrame.about;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import static java.awt.Image.SCALE_FAST;

/**
 * <code>AboutAuthorDialog</code> is an extension of <code>JDialog</code>
 * that contains information about author of the project.
 *
 * @author Kotikov S.G.
 */
public class AboutAuthorDialog extends JDialog {
    /**
     * Creates dialog with information about the author.
     *
     * @param parent parent frame of the dialog. Used for centering.
     */
    public AboutAuthorDialog(JFrame parent) {
        super(parent);
        setTitle("Об авторе");
        JLabel imageLabel = createImageLabel();

        JLabel[] textLabels = new JLabel[]{
                imageLabel,
                new JLabel("Автор"),
                new JLabel("студент группы 10702418"),
                new JLabel("Котиков Степан Георгиевич"),
                new JLabel("kotikov.stepan918@gmail.com")
        };
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        Font font = new Font(Font.DIALOG, Font.BOLD, 16);
        for (JLabel label : textLabels) {
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            textPanel.add(label);
        }
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> dispose());
        add(textPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setModal(true);
        setVisible(true);
    }

    private JLabel createImageLabel() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource("author.jpg");
        if (url == null) {
            return new JLabel("Не удалось загрузить фото");
        }
        Image image = new ImageIcon(url).getImage();
        Image scaledImage = image.getScaledInstance(250, 350, SCALE_FAST);
        return new JLabel(new ImageIcon(scaledImage));
    }
}
