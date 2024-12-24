package it.unibo.exam.bin;

import it.unibo.exam.api.*;
import it.unibo.exam.inteface.PuzzleRoom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class PuzzleRoom2 extends Room implements PuzzleRoom {

    private boolean puzzleSolved = false;
    private final int endX;   // The x-coordinate for the end of the room
    private final int endY;   // The y-coordinate for the end
    private final int interactionAreaWidth = 50;
    private final int interactionAreaHeight = 50;
    int playerX = GamePanel.ORIGINAL_HEIGHT / 2;
    int playerY = GamePanel.ORIGINAL_WIDTH / 2;
    public PuzzleRoom2(List<Door> doors) { 
        super(Color.GRAY, doors); // Set the room's background color
        this.endX = GamePanel.ORIGINAL_WIDTH - GamePanel.TILE_SIZE;
        this.endY = GamePanel.ORIGINAL_HEIGHT / 2;
    }

    @Override
    public void updatePuzzleLogic(KeyHandler keyHandler) {
        if (!puzzleSolved && playerX >= endX - interactionAreaWidth / 2 &&
            playerX <= endX + interactionAreaWidth / 2 &&
            playerY >= endY - interactionAreaHeight / 2 &&
            playerY <= endY + interactionAreaHeight / 2 &&
            keyHandler.interactPressed) {
            puzzleSolved = true;
            System.out.println("Puzzle Solved in PuzzleRoom1!");
        }
    }

    @Override
    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2); // Draw the basic room
        if (!puzzleSolved) {
            g2.setColor(Color.RED); // Color for the interaction area
            g2.fillRect(endX - interactionAreaWidth / 2, endY - interactionAreaHeight / 2, 
                        interactionAreaWidth, interactionAreaHeight);
        } else {
            g2.setColor(Color.GREEN);
            g2.drawString("Puzzle Solved!", GamePanel.ORIGINAL_WIDTH / 2 - 50, GamePanel.ORIGINAL_HEIGHT / 2);
        }
    }
}
