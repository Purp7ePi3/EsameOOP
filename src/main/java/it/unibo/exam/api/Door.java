package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Door {
    private int x;
    private int y;
    private String name;
    private int width;
    private int height;
    private Rectangle hitbox;
    
    public Door(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.width = GamePanel.TILE_SIZE;
        this.height = GamePanel.TILE_SIZE;
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean isPlayerNearby(int playerX, int playerY) {
        // Check if the player is close to the door
        int playerSize = GamePanel.TILE_SIZE;
        Rectangle playerHitbox = new Rectangle(playerX, playerY, playerSize, playerSize);
        return hitbox.intersects(playerHitbox);
    }

    public String interact() {
        // Handle interaction logic, e.g., load a new map
        return "(" + x + ", " + y + ")";

    }
    
    public String getName(){
        return this.name;
    }
}

