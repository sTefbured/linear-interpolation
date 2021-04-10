package linearInterpolation.userInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SplashScreen extends JFrame {
    private JButton nextButton;
    private JButton exitButton;
    private BufferedImage splashScreenImage;

    public SplashScreen() {
        try {
            splashScreenImage = ImageIO.read(new File("res/splashscreen.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(new GridBagLayout());

        setUndecorated(true);
        addImageLabel();
        initializeNextButton();
        initializeExitButton();
        addButtons();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addImageLabel() {
        JLabel label = new JLabel(new ImageIcon(splashScreenImage));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        getContentPane().add(label, constraints);
    }

    private void addButtons() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.ipady = 40;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(nextButton, constraints);
        constraints.gridx = 1;
        getContentPane().add(exitButton, constraints);
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
        exitButton.addActionListener(e -> System.exit(0));
    }
}
