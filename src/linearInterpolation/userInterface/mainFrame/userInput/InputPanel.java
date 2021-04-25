package linearInterpolation.userInterface.mainFrame.userInput;

import linearInterpolation.userInterface.mainFrame.userInput.inner.*;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    private final InitializingPanel initializingPanel;
    private final NewPointsPanel newPointsPanel;

    public InputPanel() {
        setLayout(new GridLayout(1, 2));
        initializingPanel = new InitializingPanel();
        add(initializingPanel);
        newPointsPanel = new NewPointsPanel();
        add(newPointsPanel);
    }

    public NewPointsPanel getNewPointsPanel() {
        return newPointsPanel;
    }
}
