package it.unibo.exam.bin;

import it.unibo.exam.api.*;
import it.unibo.exam.inteface.PuzzleRoom;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class PuzzleRoom1 extends Room implements PuzzleRoom {
    private boolean puzzleSolved = false;
    private final GamePanel gamePanel;

    public PuzzleRoom1(List<Door> doors, GamePanel gamePanel) {
        super(Color.gray, doors); // Default room color
        this.gamePanel = gamePanel;
    }

    @Override
    public void updatePuzzleLogic(KeyHandler keyHandler) {
        int playerX = gamePanel.getPlayerX();
        int playerY = gamePanel.getPlayerY();
        
        if (!getDoors().isEmpty()) {
            Door firstDoor = getDoors().get(0);
            
            if (playerX == 0 && playerY == 0 && keyHandler.interactPressed) {
                puzzleSolved = true;
                firstDoor.setSolved(true);
                // Aggiorna anche la porta nella stanza principale
                gamePanel.updateDoorState(1, true); // 1 è l'indice della stanza del puzzle
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        
        if (puzzleSolved) {
            g2.setColor(Color.GREEN);
            //setColor(Color.PINK);  // Assuming you have a method to set the color
            g2.drawString("Puzzle Solved!", GamePanel.ORIGINAL_WIDTH / 2 - 50, GamePanel.ORIGINAL_HEIGHT / 2);
        }
    }

    @Override
    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }
}