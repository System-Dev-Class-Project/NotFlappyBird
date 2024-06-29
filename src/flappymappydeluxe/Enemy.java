package flappymappydeluxe;

import java.awt.Graphics;

// Enemy interface representing common methods for all enemy types
public interface Enemy {
	
    void setVisible(boolean b); // Set visibility of the enemy
    
    void spawn(WallImage wall); // Spawn the enemy near a wall
    
    void handleCollision(); // Handle collision with the bird
    
    void drawPowerUp(Graphics g); // Draw the enemy
    
    void moveEnemy(WallImage wi); // Move the enemy
    
}
