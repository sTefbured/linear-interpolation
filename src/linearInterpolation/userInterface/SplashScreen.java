package linearInterpolation.userInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends JFrame {
    private JButton nextButton;
    private JButton exitButton;
    private BufferedImage splashScreenImage;
    private final Timer timer;

    @SuppressWarnings("ConstantConditions")
    public SplashScreen() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            InputStream resourceStream = classLoader
                    .getResourceAsStream("splashscreen.jpg");
            splashScreenImage = ImageIO.read(resourceStream);
        } catch (IOException | IllegalArgumentException e) {
            splashScreenImage = new BufferedImage(800, 600,
                    BufferedImage.TYPE_INT_ARGB);
        }
        int delayMilliseconds = 60000;
        timer = new Timer(delayMilliseconds, e -> System.exit(0));

        setLayout(new GridBagLayout());
        setUndecorated(true);
        addImageLabel();
        initializeNextButton();
        initializeExitButton();
        addImageLabel();
        addButtons();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        timer.start();
    }

    private void addImageLabel() {
        JLabel label = new JLabel(new ImageIcon(splashScreenImage));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
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
            timer.stop();
            dispose();
        });
    }

    private void initializeExitButton() {
        exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> System.exit(0));
    }
}
