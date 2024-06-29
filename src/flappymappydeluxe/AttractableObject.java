package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;

// This interface defines the behaviors for objects that can be attracted to the player (like coins and powerups)
public interface AttractableObject {

    // Checks if the object is visible
    boolean isVisible();

    // Moves the object towards the player
    void moveToPlayer(int x, int y);

    // Get the bounding rectangle of the object for collision detection
    Rectangle getRect();

    // Getter for x-coordinate of the object
    int getX();

    // Getter for y-coordinate of the object
    int getY();

    // Set the x-coordinate of the object
    void setX(int i);

    // Set the y-coordinate of the object
    void setY(int i);

    // Set the visibility of the object
    void setVisible(boolean b);

    // Spawn the object relative to a wall
    void spawn(WallImage wall);

    // Draw the object as a power-up
    void drawPowerUp(Graphics g);

    // Move the power-up relative to wall and bird
    void movePowerUp(WallImage wi, BirdTestAnimation bi);
}
