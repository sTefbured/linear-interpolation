package linearInterpolation.userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SplashScreen extends JFrame {
    private JButton nextButton;
    private JButton exitButton;

    public SplashScreen() {
        Dimension screenSize = getToolkit().getScreenSize();
        setSize(screenSize.width * 3 / 7, screenSize.height / 2);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        addTextPanel();
        initializeNextButton();
        initializeExitButton();
        addButtons();

        add(nextButton);
        add(exitButton);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
        setUndecorated(true);
        setVisible(true);
    }

    //TODO: implement
    private void addTextPanel() {

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        //add(textPanel, constraints);
    }

    private void addButtons() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(nextButton, constraints);
        constraints.gridx = 2;
        constraints.gridy = 1;
        add(exitButton, constraints);
    }

    private void initializeNextButton() {
        nextButton = new JButton("Далее");
        nextButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });
    }

    private void initializeExitButton() {
        exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }
}
