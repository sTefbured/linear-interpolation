package linearInterpolation.userInterface.mainFrame.userInput;

import linearInterpolation.userInterface.mainFrame.userInput.inner.InitializingPanel;
import linearInterpolation.userInterface.mainFrame.userInput.inner.NewPointsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * InputPanel is an extended version of JPanel that contains InitializingPanel
 * and NewPointsPanel objects.
 *
 * @author Kotikov S.G.
 */
public class InputPanel extends JPanel {
    private final InitializingPanel initializingPanel;
    private final NewPointsPanel newPointsPanel;

    /**
     * Creates an InputPanel object with added InitializingPanel
     * and NewPointsPanel.
     */
    public InputPanel() {
        setLayout(new GridLayout(1, 2));
        initializingPanel = new InitializingPanel();
        add(initializingPanel);
        newPointsPanel = new NewPointsPanel();
        add(newPointsPanel);
    }

    /**
     * Get initializingPanel.
     *
     * @return initializingPanel.
     */
    public InitializingPanel getInitializingPanel() {
        return initializingPanel;
    }

    /**
     * Get newPointsPanel.
     *
     * @return newPointsPanel.
     */
    public NewPointsPanel getNewPointsPanel() {
        return newPointsPanel;
    }
}
