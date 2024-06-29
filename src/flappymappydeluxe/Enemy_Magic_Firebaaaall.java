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
 * The Enemy_Magic_Firebaaaall class represents a fireball enemy in the game.
 * It implements the Enemy interface and includes methods for movement, collision detection,
 * and rendering the enemy on the screen.
 */
public class Enemy_Magic_Firebaaaall implements Enemy {
    private static BufferedImage GuImg; // Image of the fireball
    public static int x; // X-position of the fireball
    public static int y; // Y-position of the fireball
    private final int diameter = 100; // Size of the fireball
    private static boolean visible = false; // Fireball visibility
    private BirdTestAnimation player; // Reference to the player
    int speed = 15; // Speed of the fireball
    private Timer collisionTimer = null; // Timer for collision handling
    
    private AudioPlayer audioPlayer; // Reference to the audio player for playing sounds

    /**
     * Constructor for the Enemy_Magic_Firebaaaall class.
     * 
     * @param player Reference to the player object
     * @param wall Reference to the wall object
     * @param audioPlayer Reference to the audio player object
     */
    public Enemy_Magic_Firebaaaall(BirdTestAnimation player, WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.player = player;
        loadMagnetImage();
    }

    @Override
    public void setVisible(boolean b) {
        // Set the visibility of the fireball
        visible = b;
    }

    /**
     * Loads the image of the fireball from the file system.
     */
    private void loadMagnetImage() {
        try {
            GuImg = ImageIO.read(new File("NotFlappyBird-main/Images/Fireball.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the fireball on the screen if it is visible.
     * 
     * @param g Graphics object used for drawing
     */
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(GuImg, x, y, null);
            // System.out.println("Fireball drawn");
        }
    }

    /**
     * Returns the rectangular bounds of the fireball for collision detection.
     * 
     * @return Rectangle representing the bounds of the fireball
     */
    public Rectangle getEnemypRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the fireball across the screen and handles its behavior when it goes off-screen.
     * 
     * @param wall Reference to the wall object
     */
    public void moveEnemy(WallImage wall) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()) - speed; // Move the fireball at the same speed as the wall

        if (x < -diameter - 1000) { // If the fireball moves off-screen
            visible = false; // Make the fireball invisible
        }

        if (GamePanel.GameOver) {
            visible = false; // Make the fireball invisible if the game is over
        }

        // Uncomment the following block to set the fireball's position based on the wall's position
        /*
        if (GamePanel.score % 4 == 0) {
            visible = true;
            // Set the fireball's position relative to the wall
            this.x = wall.X + 300;
            this.y = wall.Y - (WallImage.gap / 2) - (diameter / 2);
        }
        */

        handleCollision();
    }

    /**
     * Handles the collision between the fireball and the bird.
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
                            collisionTimer = null; // Reset the timer
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
        // Set the fireball's position relative to the wall
        this.x = wall.X + 300;
        this.y = wall.Y - (WallImage.gap / 2) - (diameter / 2);
    }
}
