package it.unibo.exam.api;

public interface PuzzleRoom {
    // Method signature to accept KeyHandler and player position
    void updatePuzzleLogic(KeyHandler keyH, int playerX, int playerY);
}
