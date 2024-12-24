package it.unibo.exam.bin;

import it.unibo.exam.api.*;
import it.unibo.exam.inteface.PuzzleRoom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class PuzzleRoom5 extends Room implements PuzzleRoom {

    private boolean puzzleSolved = false;
    private final int buttonWidth = 50;  // Width of the green button
    private final int buttonHeight = 50; // Height of the green button
    private final int doorWidth = 50;    // Width of the door to go back
    private final int doorHeight = 50;   // Height of the door to go back
    int playerX = 800 / 2;
    int playerY = 300;

    // Positions for the green button and the back door
    private final int buttonX = GamePanel.ORIGINAL_WIDTH - buttonWidth - 20;
    private final int buttonY = GamePanel.ORIGINAL_HEIGHT - buttonHeight - 20;
    private final int doorX = 20;
    private final int doorY = 20;

    public PuzzleRoom5(List<Door> doors) {
        super(Color.GRAY, doors); // Set the room's background color
    }

    @Override
    public void updatePuzzleLogic(KeyHandler keyHandler) {
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
            // Here you would likely change the current room index to 0 or the index of the previous room
        }
    }

    @Override
    public boolean isPuzzleSolved() {
        return puzzleSolved;  // Return true if the puzzle is solved
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);  // Call the parent class draw method to draw basic room elements
        
        // Draw the green button if the puzzle is not solved
        if (!puzzleSolved) {
            g2.setColor(Color.GREEN);  // Color for the green button
            g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
            g2.setColor(Color.BLACK);
            g2.drawString("Solve Puzzle", buttonX + 5, buttonY + 25);
        } else {
            // Puzzle solved state (e.g., display a message)
            g2.setColor(Color.GREEN);
            g2.drawString("Puzzle Solved!", GamePanel.ORIGINAL_WIDTH / 2 - 50, GamePanel.ORIGINAL_HEIGHT / 2);
        }

        // Draw the back door in the top-left corner
        g2.setColor(Color.BLUE);
        g2.fillRect(doorX, doorY, doorWidth, doorHeight);
        g2.setColor(Color.BLACK);
        g2.drawString("Back", doorX + 5, doorY + 25);  // Text indicating that it goes back to the previous room
    }
}
