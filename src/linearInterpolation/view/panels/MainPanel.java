package linearInterpolation.view.panels;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        setLocation(100, 100);
        setLayout(new GridLayout(1, 1));
        setPreferredSize(new Dimension(400, 300));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("New Tab", new ValuesInputPanel());

        add(tabbedPane);
    }
}
