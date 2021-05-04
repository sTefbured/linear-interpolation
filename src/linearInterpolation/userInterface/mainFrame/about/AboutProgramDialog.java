package linearInterpolation.userInterface.mainFrame.about;

import javax.swing.*;
import java.awt.*;

public class AboutProgramDialog extends JDialog {
    private static final String TITLE = "About program";
    private static final String PROGRAM_NAME = "Linear interpolation and extrapolation";

    public AboutProgramDialog(JFrame parent) {
        super(parent, TITLE);
        setLayout(new GridBagLayout());
        setSize(200, 300);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
