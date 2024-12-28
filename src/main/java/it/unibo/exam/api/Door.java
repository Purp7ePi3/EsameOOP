package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Door {
    private int x;
    private int y;
    private String name;
    private int targetRoomIndex;
    private int width;
    private int height;
    private Rectangle hitbox;
    private boolean isSolved;

    public Door(int x, int y, String name, int targetRoomIndex, boolean isSolved) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.targetRoomIndex = targetRoomIndex;
        this.isSolved = isSolved;
        this.width = GamePanel.TILE_SIZE;
        this.height = GamePanel.TILE_SIZE;
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public int getTargetRoomIndex() {
        return targetRoomIndex;
    }

    public void draw(Graphics2D g) {
        g.setColor(isSolved ? Color.GREEN : Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean isPlayerNearby(int playerX, int playerY) {
        int detectionRange = GamePanel.TILE_SIZE;
        Rectangle detectionArea = new Rectangle(
            x - detectionRange/2,
            y - detectionRange/2,
            width + detectionRange,
            height + detectionRange
        );
        Rectangle playerHitbox = new Rectangle(playerX, playerY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        return detectionArea.intersects(playerHitbox);
    }

    public String getName() {
        return this.name;
    }

    public boolean isSolved() {
        return this.isSolved;
    }

    public void setSolved(boolean solved) {
        this.isSolved = solved;
    }
}