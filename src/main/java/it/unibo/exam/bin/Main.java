package it.unibo.exam.bin;

import javax.swing.JFrame;
import it.unibo.exam.api.GamePanel;
import it.unibo.exam.api.KeyHandler;

/**
 * Entry point for the game application.
 */
public final class Main {

    // Private constructor to prevent instantiation
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    }

    /**
     * Main method to initialize the game window and start the game loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args) {
        // Create the key handler instance
        KeyHandler keyHandler = new KeyHandler();

        // Create the main game window
        final JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("UNIBO");

        // Add the game panel to the window, passing the key handler
        final GamePanel gamePanel = new GamePanel(keyHandler);
        window.add(gamePanel);
        window.pack(); // Sizes the window to fit the preferred size of its components
        window.setLocationRelativeTo(null); // Center the window on the screen
        window.setVisible(true);
        // Start the game loop
        gamePanel.startGameThread();
    }
}
