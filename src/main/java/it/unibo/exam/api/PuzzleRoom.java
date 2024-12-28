package it.unibo.exam.api;  // Changed from inteface to api

import java.awt.Graphics2D;

public interface PuzzleRoom {
    void updatePuzzleLogic(KeyHandler keyHandler);
    boolean isPuzzleSolved();
    void draw(Graphics2D g2);
}