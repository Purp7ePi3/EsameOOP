// Main.java
package it.unibo.exam.bin;

import javax.swing.JFrame;
import it.unibo.exam.api.GamePanel;
import it.unibo.exam.api.KeyHandler;

public final class Main {
    private Main() {
        throw new UnsupportedOperationException("Main class cannot be instantiated");
    }

    public static void main(final String[] args) {
        KeyHandler keyHandler = new KeyHandler();

        final JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("UNIBO");

        final GamePanel gamePanel = new GamePanel(keyHandler);
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.startGameThread();
    }
}

/***
 * @Copyright
 *            Simone Brunelli - Purp7ePi3 - simone.brunelli3@studio.unibo.it
 *            Tommaso Nori - tommaso.nori@studio.unibo.it
 *            Daniel Horna - d.alejandrohorna@gmail.com
 *            Davide Amantini - AmantiniDavide - davide.amantini@studio.unibo.it
 *            Mattia Pozzati - PozzatiMattia - mattia.pozzati3@studio.unibo.it
 */