package linearInterpolation;

import linearInterpolation.userInterface.splashScreen.SplashScreen;

import javax.swing.*;

/**
 * Entry class of the application
 *
 * @author Kotikov S.G.
 */
public class Main {
    /**
     * Entry point of the application.
     * Creates SplashScreen object in the AWT event dispatching thread.
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}
