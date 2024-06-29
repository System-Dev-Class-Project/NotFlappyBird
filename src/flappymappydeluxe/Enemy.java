/**
 * The Enemy interface defines the contract for enemy objects in the Flappy Mappy Deluxe game.
 * Implementing this interface requires providing methods for managing visibility, spawning,
 * handling collisions, drawing power-ups, and moving the enemy. This approach ensures a consistent
 * structure across different enemy types, facilitating smoother integration with game components
 * like GamePanel and Difficulty Management.
 * 
 * While many implementations of these methods may be similar across various enemy classes, the use
 * of an interface instead of a superclass allows for greater flexibility. This is particularly important
 * in an open-source project, where contributors may need to create unique enemy behaviors, such as
 * an instant death enemy. By adhering to this interface, future developers can introduce diverse
 * and innovative enemy types without being constrained by a common superclass implementation.
 */
package flappymappydeluxe;

import java.awt.Graphics;

public interface Enemy {
    
    /**
     * Sets the visibility of the enemy.
     * 
     * @param b true if the enemy should be visible, false otherwise.
     */
    void setVisible(boolean b);

    /**
     * Spawns the enemy at a specific location based on the given WallImage object.
     * This method defines the initial position and state of the enemy.
     * 
     * @param wall The WallImage object used to determine the spawn location of the enemy.
     */
    void spawn(WallImage wall);

    /**
     * Handles the logic when the enemy collides with another object.
     * This method should define the behavior of the enemy upon collision.
     */
    void handleCollision();

    /**
     * Draws the enemy's power-up on the screen.
     * This method is responsible for rendering the power-up image at the enemy's current position.
     * 
     * @param g The Graphics object used to draw the power-up.
     */
    void drawPowerUp(Graphics g);

    /**
     * Moves the enemy based on the given WallImage object.
     * This method should update the enemy's position and state as the game progresses.
     * 
     * @param wi The WallImage object used to determine the enemy's movement.
     */
    void moveEnemy(WallImage wi);
}
