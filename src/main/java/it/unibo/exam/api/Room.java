package it.unibo.exam.api;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Room {
    private Color backgroundColor; // Colore di sfondo della stanza
    private List<Door> doors;      // Lista di porte nella stanza

    public Room(Color backgroundColor, List<Door> doors) {
        this.backgroundColor = backgroundColor;
        this.doors = doors;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void draw(Graphics g) {
        // Disegna lo sfondo della stanza
        g.setColor(backgroundColor);
        g.fillRect(0, 0, GamePanel.ORIGINAL_WIDTH, GamePanel.ORIGINAL_HEIGHT);

        // Disegna tutte le porte
        for (Door door : doors) {
            door.draw(g);
        }
    }

    // Aggiungi il metodo updatePuzzleLogic
    public void updatePuzzleLogic(KeyHandler keyH) {
        // Logica base per la stanza. Potrebbe essere vuota o avere logica generica.
    }
}