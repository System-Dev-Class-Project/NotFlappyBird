package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents an Enemy_Alien object in the game, which interacts with the player and wall.
 */
public class Enemy_Alien implements Enemy {

    // Static variables
    private static BufferedImage GuImg; // Image for the enemy
    public static int x; // X-coordinate of the enemy
    public static int y; // Y-coordinate of the enemy
    private static boolean visible = false; // Visibility state of the enemy

    // Instance variables
    private final int diameter = 100; // Size of the enemy
    private BirdTestAnimation player; // The player object
    private int vy = 8; // Vertical velocity
    private int upperBound = 0; // Top of the screen
    private int lowerBound = 600; // Bottom of the screen
    public boolean hit = true; // Hit state
    private Timer collisionTimer = null; // Timer for collision handling
    private AudioPlayer audioPlayer; // Audio player for playing sound effects

    /**
     * Constructs an Enemy_Alien object.
     *
     * @param player the player object
     * @param wall the wall object
     * @param audioPlayer the audio player object
     */
    public Enemy_Alien(BirdTestAnimation player, WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.player = player;
        loadMagnetImage();
    }

    /**
     * Sets the visibility of the enemy.
     *
     * @param b the visibility state to set
     */
    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    /**
     * Loads the image for the enemy.
     */
    private void loadMagnetImage() {
        try {
            GuImg = ImageIO.read(new File("NotFlappyBird-main/Images/Alien.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the enemy on the screen if it is visible.
     *
     * @param g the Graphics object to draw on
     */
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(GuImg, x, y, null);
        }
    }

    /**
     * Returns a rectangle representing the enemy's bounding box.
     *
     * @return the bounding box of the enemy
     */
    public Rectangle getEnemypRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the enemy based on the game's state and interactions with the player and wall.
     *
     * @param wall the wall object
     */
    public void moveEnemy(WallImage wall) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the enemy at the same speed as the wall

        // If the enemy moves off-screen, make it invisible
        if (x < -diameter - 1000) {
            visible = false;
        }

        y += vy;

        // Reverse direction if the enemy hits the top or bottom of the screen
        if (y <= upperBound || y + diameter >= lowerBound) {
            vy *= -1;
        }

        // If the game is over, make the enemy invisible
        if (GamePanel.GameOver) {
            visible = false;
        }

        handleCollision();
    }

     /**
 * Handles the collision between the enemy and the bird.
 * If the bird is not invincible, checks for collision and responds accordingly.
 */
public void handleCollision() {
    // Check if the bird is invincible
    if (!InvincibilityPower.isInvincible()) {
        Rectangle enemyRect = getEnemypRect();
        Rectangle birdRect = BirdTestAnimation.getBirdRect();

        // Collision detection with one or fewer hearts
        if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() <= 1) {
            // Play hurt and game over sounds
            audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
            // Send the score to the server
            GamePanel.sendScoreToServer(GamePanel.score); 

            // Show pop-up message and handle the selected option
            int option = GamePanel.popUpMessage();
            if (option == 0) {  // Retry
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                BirdTestAnimation.reset();
            } else if (option == 2) {  // Exit
                JFrame frame = FlappyClass.getWindow();
                MenuPanel.audioPlayer.stop();    
                frame.dispose();
                FlappyClass.timer.stop();
            } else {  // Return to menu
                MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                BirdTestAnimation.reset();
                FlappyClass.timer.stop();
                FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
            }
        } 
        // Collision detection with more than one heart
        else if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() > 1) {
            audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            // If there is no active collision timer, create one to handle heart loss and invincibility reset
            if (collisionTimer == null || !collisionTimer.isRunning()) { 
                collisionTimer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { 	
                        HeartsPowerUp.subHeart(); // Subtract a heart
                        System.out.println("Heart lost to Batman! Current hearts: " + HeartsPowerUp.getHearts());
                        collisionTimer = null; //reset the timer
                    }
                });
                collisionTimer.setRepeats(false); // Set the timer to run once
                collisionTimer.start(); // Start the timer
            }
        }
    }
}
    

    

    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 500-(diameter/2); // Sets the center of the alien 500 pixels to the right of the wall 
        this.y = wall.Y - (WallImage.gap / 2)-(diameter/2); // Sets the center of the alien to the center of the gap in the wall
    }

}