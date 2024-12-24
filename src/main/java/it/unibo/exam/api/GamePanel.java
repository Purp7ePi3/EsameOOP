package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    static final int ORIGINAL_WIDTH = 800;
    static final int ORIGINAL_HEIGHT = 600;
    static final int ORIGINAL_TILE_SIZE = 16;
    static final int SCALE = 3;
    static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    KeyHandler keyH;

    private Thread gameThread;
    final int speed = 5;
    int playerX;
    int playerY;

    private int fps;
    private long lastFpsTime;
    private int frameCount;

    private ArrayList<Door> doors;

    // Memorizza la posizione del giocatore come frazione
    private double playerXPercentage;
    private double playerYPercentage;

    public GamePanel(KeyHandler keyHandler) {
        this.keyH = keyHandler;
        this.setPreferredSize(new Dimension(ORIGINAL_WIDTH, ORIGINAL_HEIGHT)); // Set initial window size
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Inizializza la posizione del giocatore come frazione
        playerX = ORIGINAL_WIDTH / 2 - TILE_SIZE / 2; // Posizione iniziale
        playerY = ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2; // Posizione iniziale

        // Calcola la posizione come frazione della finestra originale
        playerXPercentage = (double) playerX / ORIGINAL_WIDTH;
        playerYPercentage = (double) playerY / ORIGINAL_HEIGHT;

        // Inizializza le porte
        doors = new ArrayList<>();
        updateDoors();

        // Gestisci il ridimensionamento della finestra
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePlayerPosition(); // Aggiorna la posizione del giocatore al ridimensionamento
                updateDoors();           // Aggiorna la posizione delle porte
            }
        });
    }

    private void updatePlayerPosition() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Ricalcola la posizione del giocatore in base alla frazione della finestra corrente
        playerX = (int) (playerXPercentage * panelWidth);
        playerY = (int) (playerYPercentage * panelHeight);
    }

    private void updateDoors() {
        doors.clear();
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        doors.add(new Door(0, panelHeight / 4 - TILE_SIZE / 2, "Top-left Door"));
        doors.add(new Door(0, 3 * panelHeight / 4 - TILE_SIZE / 2, "Bottom-left Door"));
        doors.add(new Door(panelWidth - TILE_SIZE, panelHeight / 4 - TILE_SIZE / 2, "Top-right Door"));
        doors.add(new Door(panelWidth - TILE_SIZE, 3 * panelHeight / 4 - TILE_SIZE / 2, "Bottom-right Door"));
        doors.add(new Door(panelWidth / 2 - TILE_SIZE / 2, panelHeight - TILE_SIZE, "Bottom-center Door"));
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        final int FPS = 60; // Target FPS
        final long frameTime = 1_000_000_000 / FPS; // Nanoseconds per frame
        long lastTime = System.nanoTime();
        long currentTime;
        double deltaTime;

        lastFpsTime = System.currentTimeMillis();
        frameCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / 1_000_000_000.0; // Convert nanoseconds to seconds

            update(deltaTime); // Pass deltaTime to update
            repaint();         // Render the frame

            frameCount++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                fps = frameCount;
                frameCount = 0;
                lastFpsTime = System.currentTimeMillis();
            }

            long sleepTime = frameTime - (System.nanoTime() - currentTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000); // Convert nanoseconds to milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            lastTime = currentTime;
        }
    }

    String lastInteraction = "";

    public void update(double deltaTime) {
        int currentSpeedX = 0;
        int currentSpeedY = 0;
        if (keyH.downPressed) currentSpeedY = speed;
        if (keyH.upPressed) currentSpeedY = -speed;
        if (keyH.leftPressed) currentSpeedX = -speed;
        if (keyH.rightPressed) currentSpeedX = speed;

        if ((keyH.upPressed || keyH.downPressed) && (keyH.leftPressed || keyH.rightPressed)) {
            double length = Math.sqrt(currentSpeedX * currentSpeedX + currentSpeedY * currentSpeedY);
            currentSpeedX = (int) (currentSpeedX / length * speed);
            currentSpeedY = (int) (currentSpeedY / length * speed);
        }

        playerX += currentSpeedX * deltaTime * 100;
        playerY += currentSpeedY * deltaTime * 100;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (playerX < 0) playerX = 0;
        if (playerX > panelWidth - TILE_SIZE) playerX = panelWidth - TILE_SIZE;
        if (playerY < 0) playerY = 0;
        if (playerY > panelHeight - TILE_SIZE) playerY = panelHeight - TILE_SIZE;

        // Aggiorna la posizione percentuale del giocatore
        playerXPercentage = (double) playerX / panelWidth;
        playerYPercentage = (double) playerY / panelHeight;

        // Check for interactions with doors
        for (Door door : doors) {
            if (door.isPlayerNearby(playerX, playerY) && keyH.interactPressed) {
                lastInteraction = "Interacted with: " + door.getName() + door.interact();
                door.interact();
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
    
        // Draw player
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, TILE_SIZE, TILE_SIZE);
    
        // Draw doors
        for (Door door : doors) {
            door.draw(g2);
        }
    
        // Set font and prepare text rendering
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(20f));
        FontMetrics metrics = g2.getFontMetrics();
    
        // Draw FPS counter
        String fpsText = "FPS: " + fps;
        g2.drawString(fpsText, getWidth() - metrics.stringWidth(fpsText) - 20, 30);
        
        // Center the interaction text
        if (!lastInteraction.isEmpty()) {
            int textWidth = metrics.stringWidth(lastInteraction);
            
            int textX = (getWidth() - textWidth) / 2;
            g2.drawString(lastInteraction, textX, 30);
        }
        
        g2.dispose();
    }
}
