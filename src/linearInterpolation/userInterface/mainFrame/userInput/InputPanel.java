package linearInterpolation.userInterface.mainFrame.userInput;

import linearInterpolation.userInterface.mainFrame.userInput.inner.InitializingPanel;
import linearInterpolation.userInterface.mainFrame.userInput.inner.NewPointsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * <code>InputPanel</code> is an extended version of <code>JPanel</code>
 * that contains <code>InitializingPanel</code> and <code>NewPointsPanel</code> objects.
 *
 * @author Kotikov S.G.
 * @see InitializingPanel
 * @see NewPointsPanel
 */
public class InputPanel extends JPanel {
    private final InitializingPanel initializingPanel;
    private final NewPointsPanel newPointsPanel;

    /**
     * Creates an <code>InputPanel</code> object with added <code>InitializingPanel</code>
     * and <code>NewPointsPanel</code>.
     */
    public InputPanel() {
        setLayout(new GridLayout(1, 2));
        initializingPanel = new InitializingPanel();
        add(initializingPanel);
        newPointsPanel = new NewPointsPanel();
        add(newPointsPanel);
    }

    /**
     * @return <code>initializingPanel</code>.
     */
    public InitializingPanel getInitializingPanel() {
        return initializingPanel;
    }

    /**
     * @return <code>newPointsPanel</code>.
     */
    public NewPointsPanel getNewPointsPanel() {
        return newPointsPanel;
    }
}
