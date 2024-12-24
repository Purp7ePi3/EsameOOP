package it.unibo.exam.inteface;

import it.unibo.exam.api.KeyHandler;

public interface PuzzleRoom {

    void updatePuzzleLogic(KeyHandler keyHandler);

    boolean isPuzzleSolved();

    void draw(java.awt.Graphics2D g2);
}
