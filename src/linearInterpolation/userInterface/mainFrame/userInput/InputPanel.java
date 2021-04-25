package linearInterpolation.userInterface.mainFrame.userInput;

import linearInterpolation.userInterface.mainFrame.userInput.inner.*;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    private final NewPointsPanel newPointsPanel;

    public InputPanel() {
        setLayout(new GridLayout(1, 2));
        InitializingPanel initializingPanel = new InitializingPanel();
        add(initializingPanel);
        newPointsPanel = new NewPointsPanel();
        add(newPointsPanel);
    }

    public NewPointsPanel getNewPointsPanel() {
        return newPointsPanel;
    }
}
