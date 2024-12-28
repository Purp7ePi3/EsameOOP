package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Room {
    private Color backgroundColor;
    private List<Door> doors;

    public Room(Color backgroundColor, List<Door> doors) {
        this.backgroundColor = backgroundColor;
        this.doors = doors;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, GamePanel.ORIGINAL_WIDTH, GamePanel.ORIGINAL_HEIGHT);

        for (Door door : doors) {
            door.draw(g2);
        }
    }

    public void updatePuzzleLogic(KeyHandler keyH) {
    }

    public void setColor(Color color) {
        this.backgroundColor = color;
    }
}