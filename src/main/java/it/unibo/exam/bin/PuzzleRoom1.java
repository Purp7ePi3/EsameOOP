package it.unibo.exam.bin;

import it.unibo.exam.api.*;
import it.unibo.exam.inteface.PuzzleRoom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class PuzzleRoom1 extends Room implements PuzzleRoom {

    private boolean puzzleSolved = false;
    private final int buttonWidth = 50;
    private final int buttonHeight = 50;
    private final int doorWidth = 50;
    private final int doorHeight = 50;

    private final int buttonX = GamePanel.ORIGINAL_WIDTH - buttonWidth - 20;
    private final int buttonY = GamePanel.ORIGINAL_HEIGHT - buttonHeight - 20;
    private final int doorX = 20;
    private final int doorY = 20;

    private GamePanel gamePanel;  // Reference to GamePanel to get player position

    public PuzzleRoom1(List<Door> doors, GamePanel gamePanel) {
        super(Color.GRAY, doors); // Set the room's background color
        this.gamePanel = gamePanel; // Assign the game panel reference
    }

    @Override
    public void updatePuzzleLogic(KeyHandler keyHandler) {
        int playerX = 400;  // Get player position from GamePanel
        int playerY = 300;

        // Check interaction with the green button to solve the puzzle
        if (!puzzleSolved && playerX >= buttonX - buttonWidth / 2 &&
            playerX <= buttonX + buttonWidth / 2 &&
            playerY >= buttonY - buttonHeight / 2 &&
            playerY <= buttonY + buttonHeight / 2 &&
            keyHandler.interactPressed) {
            
            // Puzzle is solved when the player interacts with the green button
            puzzleSolved = true;
            System.out.println("Puzzle Solved in PuzzleRoom1!");
        }

        // Check interaction with the back door to return to the previous room
        if (playerX >= doorX - doorWidth / 2 &&
            playerX <= doorX + doorWidth / 2 &&
            playerY >= doorY - doorHeight / 2 &&
            playerY <= doorY + doorHeight / 2 &&
            keyHandler.interactPressed) {
            
            // Logic to go back to the previous room (assuming index 0 is the previous room)
            System.out.println("Returning to the previous room.");
            // Change the current room index to 0 (or the index of the previous room)
            // For example: currentRoomIndex = 0;
        }
    }

    @Override
    public boolean isPuzzleSolved() {
        return puzzleSolved;  // Return true if the puzzle is solved
    }

    @Override
    public void draw(Graphics2D g2) {
        // Check if super.draw(g2) is necessary
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, GamePanel.ORIGINAL_WIDTH, GamePanel.ORIGINAL_HEIGHT);  // Background color
        
        if (!puzzleSolved) {
            g2.setColor(Color.GREEN);
            g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight); // Button drawing
            g2.setColor(Color.BLACK);
            g2.drawString("Solve Puzzle", buttonX + 5, buttonY + 25);
        } else {
            g2.setColor(Color.GREEN);
            g2.drawString("Puzzle Solved!", GamePanel.ORIGINAL_WIDTH / 2 - 50, GamePanel.ORIGINAL_HEIGHT / 2);
        }

        g2.setColor(Color.BLUE);
        g2.fillRect(doorX, doorY, doorWidth, doorHeight);  // Door drawing
        g2.setColor(Color.BLACK);
        g2.drawString("Back", doorX + 5, doorY + 25);
    }

}
