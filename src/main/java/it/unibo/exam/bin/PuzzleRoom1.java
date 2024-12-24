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
    private boolean firstVisit = true;  // Flag to check if this is the first time entering the room

    public PuzzleRoom1(List<Door> doors, GamePanel gamePanel) {
        super(Color.GRAY, doors); // Set the room's background color
        this.gamePanel = gamePanel; // Assign the game panel reference
    }

    @Override
    public void updatePuzzleLogic(KeyHandler keyHandler) {
        // Get player position from GamePanel (dynamic instead of hardcoded values)
        int playerX = gamePanel.getPlayerX();
        int playerY = gamePanel.getPlayerY();

        // Se è la prima volta che entri nella stanza, aggiorna la mappa
        if (firstVisit) {
            updateMap();  // Call a method to update the map or do other specific logic
            firstVisit = false;  // Set the flag to false to prevent further updates
        }

        // Check if the player is at coordinates (0, 0) and interacts with the puzzle
        if (playerX == 0 && playerY == 0 && keyHandler.interactPressed) {
            // Puzzle is solved when the player is at (0, 0) and presses the interact button
            puzzleSolved = true;
            System.out.println("Puzzle Solved in PuzzleRoom1!");
        }
        //System.out.println("x:" + playerX + " y:" + playerY);  // Debugging position

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

    // Metodo per aggiornare la mappa (o eseguire altre logiche specifiche)
    private void updateMap() {
        System.out.println("Map is updated when entering PuzzleRoom1!");
        // Inserisci qui la logica per aggiornare la mappa (ad esempio, disegnare nuovi oggetti, abilitare nuovi eventi, etc.)
        // Esempio: gamePanel.updateMapState();  // chiamata a un metodo nella classe GamePanel
    }
}
