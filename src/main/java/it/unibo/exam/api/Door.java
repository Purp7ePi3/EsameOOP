package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door {
    private int x;
    private int y;
    private String name;
    private int targetRoomIndex;
    private int width;
    private int height;
    private Rectangle hitbox;
    private boolean isSolved;  // Corrected method name

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

    public void draw(Graphics g) {
        // If the door is solved, you might draw it differently (e.g., green for solved)
        g.setColor(isSolved ? Color.GREEN : Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean isPlayerNearby(int playerX, int playerY) {
        int playerSize = GamePanel.TILE_SIZE;
        Rectangle playerHitbox = new Rectangle(playerX, playerY, playerSize, playerSize);
        return hitbox.intersects(playerHitbox);
    }

    public String getName() {
        return this.name;
    }

    public boolean isSolved() {  // Renamed method
        return this.isSolved;
    }

    public void setSolved(boolean solved) {
        this.isSolved = solved;
    }
}