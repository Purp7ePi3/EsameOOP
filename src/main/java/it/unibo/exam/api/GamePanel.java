// GamePanel.java
package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import it.unibo.exam.bin.PuzzleRoom1;
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
    private List<Room> rooms;
    private int currentRoomIndex;
    private String lastInteraction = "";

    public GamePanel(KeyHandler keyHandler) {
        this.keyH = keyHandler;
        this.setPreferredSize(new Dimension(ORIGINAL_WIDTH, ORIGINAL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.rooms = new ArrayList<>();
        this.currentRoomIndex = 0;

        playerX = ORIGINAL_WIDTH / 2 - TILE_SIZE / 2;
        playerY = ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2;

        createRooms();
    }

    private void createRooms() {
        List<Door> room1Doors = List.of(
            new Door(0, ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Cucina", 1, false),
            new Door(0, 3 * ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Gabinetto", 2, false),
            new Door(ORIGINAL_WIDTH - TILE_SIZE, ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Stanza 3", 3, false),
            new Door(ORIGINAL_WIDTH - TILE_SIZE, 3 * ORIGINAL_HEIGHT / 4 - TILE_SIZE / 2, "Stanza 4", 4, false),
            new Door(ORIGINAL_WIDTH / 2 - TILE_SIZE / 2, ORIGINAL_HEIGHT - TILE_SIZE, "Stanza 5", 5, false)
        );
        rooms.add(new Room(Color.BLUE, room1Doors));
    
        rooms.add(new PuzzleRoom1(List.of(
            new Door(ORIGINAL_WIDTH - TILE_SIZE, ORIGINAL_HEIGHT - TILE_SIZE, "Back to Main", 0, false)
        ), this));
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1_000_000_000 / FPS;
        long lastTime = System.nanoTime();
        long currentTime;
        double deltaTime;

        lastFpsTime = System.currentTimeMillis();
        frameCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / 1_000_000_000.0;

            update(deltaTime);
            repaint();

            frameCount++;
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                fps = frameCount;
                frameCount = 0;
                lastFpsTime = System.currentTimeMillis();
            }

            long sleepTime = frameTime - (System.nanoTime() - currentTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000);
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

        if (keyH.downPressed) currentSpeedY = speed;
        if (keyH.upPressed) currentSpeedY = -speed;
        if (keyH.leftPressed) currentSpeedX = -speed;
        if (keyH.rightPressed) currentSpeedX = speed;

        playerX += currentSpeedX * deltaTime * speed;
        playerY += currentSpeedY * deltaTime * speed;

        if (playerX < 0) playerX = 0;
        if (playerX > ORIGINAL_WIDTH - TILE_SIZE) playerX = ORIGINAL_WIDTH - TILE_SIZE;
        if (playerY < 0) playerY = 0;
        if (playerY > ORIGINAL_HEIGHT - TILE_SIZE) playerY = ORIGINAL_HEIGHT - TILE_SIZE;

        Room currentRoom = rooms.get(currentRoomIndex);

        if (currentRoom instanceof PuzzleRoom) {
            ((PuzzleRoom) currentRoom).updatePuzzleLogic(keyH);
        }

        for (Door door : currentRoom.getDoors()) {
            if (door.isPlayerNearby(playerX, playerY) && keyH.interactPressed) {
                lastInteraction = "Interacted with: " + door.getName();
                /* TOGLIERE IL COMMENTO A QUESTA PARTE SE UNA VOLTA RISOLTO NON SI POSSA PIù ANDARE NELLA STANZA */
                /*if (!door.isSolved() || door.getTargetRoomIndex() == 0) {
                    currentRoomIndex = door.getTargetRoomIndex();
                    playerX = ORIGINAL_WIDTH / 2 - TILE_SIZE / 2;
                    playerY = ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2;
                } else {
                    lastInteraction = "The puzzle is already solved!";
                }*/

                
                currentRoomIndex = door.getTargetRoomIndex();
                playerX = ORIGINAL_WIDTH / 2 - TILE_SIZE / 2;
                playerY = ORIGINAL_HEIGHT / 2 - TILE_SIZE / 2;
                break;
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        Room currentRoom = rooms.get(currentRoomIndex);
        if (currentRoom instanceof PuzzleRoom) {
            ((PuzzleRoom) currentRoom).draw(g2);
        } else {
            currentRoom.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, TILE_SIZE, TILE_SIZE);

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

        String roomIndexText = "Current Room Index: " + currentRoomIndex;
        g2.drawString(roomIndexText, 20, 30);

        g2.dispose();
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void updateDoorState(int targetRoomIndex, boolean solved) {
        Room mainRoom = rooms.get(0); // La stanza principale
        for (Door door : mainRoom.getDoors()) {
            if (door.getTargetRoomIndex() == targetRoomIndex) {
                door.setSolved(solved);
            }
        }
    }
    
}