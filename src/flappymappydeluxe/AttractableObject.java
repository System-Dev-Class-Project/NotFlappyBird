package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The AttractableObject interface defines the behaviors for objects that can be attracted to the player in a game by the Magnet class as well as PowerUps.
 * Implementing classes must provide functionality for visibility, movement, and rendering of these objects.
 */
public interface AttractableObject {

    /**
     * Checks if the object is currently visible.
     *
     * @return true if the object is visible, false otherwise.
     */
    boolean isVisible();

    /**
     * Moves the object towards the player (was important for the MagnetClass but not used anymore).
     *
     * @param x the x-coordinate of the player.
     * @param y the y-coordinate of the player.
     */
    void moveToPlayer(int x, int y);

    /**
     * Gets the hit box of the object.
     *
     * @return the hit box of the object.
     */
    Rectangle getRect();

    /**
     * Gets the x-coordinate of the object.
     *
     * @return the x-coordinate of the object.
     */
    int getX();

    /**
     * Gets the y-coordinate of the object.
     *
     * @return the y-coordinate of the object.
     */
    int getY();

    /**
     * Sets the x-coordinate of the object.
     *
     * @param i the new x-coordinate of the object.
     */
    void setX(int i);

    /**
     * Sets the y-coordinate of the object.
     *
     * @param i the new y-coordinate of the object.
     */
    void setY(int i);

    /**
     * Sets the visibility of the object.
     *
     * @param b true to make the object visible, false to hide it.
     */
    void setVisible(boolean b);

    /**
     * Spawns the object at a specific location, usually near a wall.
     *
     * @param wall the wall image object which determines where the object will spawn.
     */
    void spawn(WallImage wall);

    /**
     * Draws the power-up object on the screen.
     *
     * @param g the Graphics object used to draw the power-up.
     */
    void drawPowerUp(Graphics g);

    /**
     * Moves the power-up object, both wi and bi originate from previous versions of the code and are not being used now.
     *
     * @param wi the WallImage object that influenced the movement in the past.
     * @param bi the BirdTestAnimation object that influenced the movement in the past.
     */
    void movePowerUp(WallImage wi, BirdTestAnimation bi);
}


