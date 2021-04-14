package linearInterpolation;

import linearInterpolation.view.SplashScreen;

import javax.swing.*;

/**
 * Entry class of the application
 *
 * @author Kotikov S.G.
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}
