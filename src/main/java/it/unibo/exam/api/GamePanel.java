package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import it.unibo.exam.bin.PuzzleRoom1;
/*import it.unibo.exam.bin.PuzzleRoom2;
import it.unibo.exam.bin.PuzzleRoom3;
import it.unibo.exam.bin.PuzzleRoom4;
import it.unibo.exam.bin.PuzzleRoom5;*/
import it.unibo.exam.inteface.PuzzleRoom;

import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    public static final int ORIGINAL_WIDTH = 800;
    public static final int ORIGINAL_HEIGHT = 600;
    static final int ORIGINAL_TILE_SIZE = 16;
    static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    private KeyHandler keyH;

    private Thread gameThread;
    final int speed = 20;
    public int playerX;
    public int playerY;

    private int fps;
    private long lastFpsTime;
    private int frameCount;

    private List<Room> rooms; // Lista delle stanze
    private int currentRoomIndex; // Indice della stanza corrente
    private String lastInteraction = "";

    public GamePanel(KeyHandler keyHandler) {
        this.keyH = keyHandler;
        this.setPreferredSize(new Dimension(ORIGINAL_WIDTH, ORIGINAL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.rooms = new ArrayList<>(); // Inizializza la lista delle stanze
        this.currentRoomIndex = 0; // Indice iniziale della stanza

        // Posizione iniziale del giocatore
        playerX = ORIGINAL_WIDTH / 2 - TILE_SIZE / 2;
        playerY = ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2;

        createRooms(); // Crea le stanze
    }

    private void createRooms() {
        // Room 1 with 5 doors in predefined positions
        List<Door> room1Doors = List.of(
            new Door(0, ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Cucina(dove sta la donna)", 1, false),
            new Door(0, 3 * ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Gabietto nella villa(qui ci sono)", 2, false),
            new Door(ORIGINAL_WIDTH - TILE_SIZE, ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Stanza del sesso", 3, false),
            new Door(ORIGINAL_WIDTH - TILE_SIZE, 3 * ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Non lo so", 4, false),
            new Door(ORIGINAL_WIDTH / 2 - TILE_SIZE / 2, ORIGINAL_HEIGHT - TILE_SIZE, "Negozio di petardi", 5, false)
        );
        rooms.add(new Room(Color.BLUE, room1Doors)); // Add Room 1 with doors
    
        // Create specific Puzzle Rooms with "Back to Room 1" doors
        rooms.add(new PuzzleRoom1(List.of(new Door(ORIGINAL_WIDTH - TILE_SIZE, ORIGINAL_HEIGHT - TILE_SIZE, "Back to Casa di ghini", 0, false)),this)); // Puzzle Room 1
        /*rooms.add(new PuzzleRoom2(List.of(new Door(0, 0, "Back to Casa di ghini", 0, false)))); // Puzzle Room 2
        rooms.add(new PuzzleRoom3(List.of(new Door(0, 0, "Back to Casa di ghini", 0, false)))); // Puzzle Room 3
        rooms.add(new PuzzleRoom4(List.of(new Door(0, 0, "Back to Casa di ghini", 0, false)))); // Puzzle Room 4
        rooms.add(new PuzzleRoom5(List.of(new Door(0, 0, "Back to Casa di ghini", 0, false)))); // Puzzle Room 5*/
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

    public void update(double deltaTime) {
        int currentSpeedX = 0;
        int currentSpeedY = 0;

        // Gestione del movimento del giocatore
        if (keyH.downPressed) currentSpeedY = speed;
        if (keyH.upPressed) currentSpeedY = -speed;
        if (keyH.leftPressed) currentSpeedX = -speed;
        if (keyH.rightPressed) currentSpeedX = speed;

        // Aggiornamento della posizione del giocatore basata sulla velocità e delta time
        playerX += currentSpeedX * deltaTime * speed;
        playerY += currentSpeedY * deltaTime * speed;

        // Evita che il giocatore esca dai bordi della finestra
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (playerX < 0) playerX = 0;
        if (playerX > panelWidth - TILE_SIZE) playerX = panelWidth - TILE_SIZE;
        if (playerY < 0) playerY = 0;
        if (playerY > panelHeight - TILE_SIZE) playerY = panelHeight - TILE_SIZE;

        // Ottieni la stanza corrente
        Room currentRoom = rooms.get(currentRoomIndex);

        // Se la stanza corrente è un PuzzleRoom, aggiorna la logica del puzzle
        if (currentRoom instanceof PuzzleRoom) {
            ((PuzzleRoom) currentRoom).updatePuzzleLogic(keyH);  // Verifica se il puzzle è stato risolto
        }

        // Controlla le interazioni con le porte nella stanza corrente
        for (Door door : currentRoom.getDoors()) {
            if (door.isPlayerNearby(playerX, playerY) && keyH.interactPressed) {
                // Se il puzzle della porta è risolto, cambia la stanza
                if (!door.isSolved()) {
                    lastInteraction = "Interacted with: " + door.getName();
                    currentRoomIndex = door.getTargetRoomIndex();  // Cambia stanza
                    playerX = GamePanel.ORIGINAL_WIDTH / 2 - TILE_SIZE / 2;  // Ripristina la posizione del giocatore
                    playerY = GamePanel.ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2; // Ripristina la posizione del giocatore
                } else {
                    // Se il puzzle della porta non è risolto, mostra il messaggio
                    lastInteraction = "Puzzle not solved yet!";
                }
                break;
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        // Disegna la stanza corrente
        Room currentRoom = rooms.get(currentRoomIndex);
        currentRoom.draw(g2);

        // Disegna il giocatore
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, TILE_SIZE, TILE_SIZE);

        // Disegna FPS e interazioni
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(20f));
        FontMetrics metrics = g2.getFontMetrics();
        String fpsText = "FPS: " + fps;
        g2.drawString(fpsText, getWidth() - metrics.stringWidth(fpsText) - 20, 30);

        if (!lastInteraction.isEmpty()) {
            int textWidth = metrics.stringWidth(lastInteraction);
            int textX = (getWidth() - textWidth) / 2;
            g2.drawString(lastInteraction, textX, 60);
        }

        // Aggiungi la stampa dell'indice della stanza
        String roomIndexText = "Current Room Index: " + currentRoomIndex;
        g2.drawString(roomIndexText, 20, 30);  // Disegna l'indice in alto a sinistra

        g2.dispose();
    }

    public int getPlayerX(){
        return playerX;
    }

    public int getPlayerY(){
        return playerY;
    }
}