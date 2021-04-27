package linearInterpolation.userInterface.mainFrame.about;

import javax.swing.*;
import java.awt.*;

/**
 * AboutAuthorDialog is an extension of JDialog that contains information
 * about author of the project.
 *
 * @author Kotikov S.G.
 */
public class AboutAuthorDialog extends JDialog {
    /**
     * Creates dialog with added information about the author.
     *
     * @param parent parent frame of the dialog.
     */
    public AboutAuthorDialog(JFrame parent) {
        super(parent);
        setTitle("About author");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        JLabel[] textLabels = new JLabel[]{
                new JLabel("Author"),
                new JLabel("student from group 10702418"),
                new JLabel("Kotikov Stepan Georgievich"),
                new JLabel("kotikov.stepan918@gmail.com")
        };
        Font font = new Font(Font.DIALOG, Font.BOLD, 16);
        for (JLabel label : textLabels) {
            label.setFont(font);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            textPanel.add(label);
        }
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        add(textPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
