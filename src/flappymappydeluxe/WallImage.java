package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// WallImage class represents the obstacles (walls) in the game.
public class WallImage {

    private Random r = new Random();
    public int X; // X-coordinate of the wall
    public int Y = r.nextInt(GamePanel.HEIGHT - 400) + 200; // Y-coordinate of the wall, random value
    public int width_Wall = 55; // Width of the wall
    private int height = GamePanel.HEIGHT - Y; // Height of the wall
    public static int gap = 200; // Gap between upper and lower walls
    public boolean hasPassed = false; // Indicates if the bird has passed the wall
    public boolean hit = true; // Indicates if the wall has been hit
    private Timer collisionTimer = null; // Timer for collision processing

    private AudioPlayer audioPlayer; // Audio player for sound effects

    public static int speed = -6; // Speed of the wall movement

    private BufferedImage img = null; // Image of the wall

    // Constructor to initialize the wall with a given X-coordinate
    public WallImage(int X) {
        this.X = X;
        LoadImage(); // Load the wall image
    }

    // Method to load the wall image from file
    private void LoadImage() {
        try {
            img = ImageIO.read(new File("NotFlappyBird-main/Images/pipe-greendoublefinal.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to set a new image for the wall
    public void setPipeImage(BufferedImage pipeImage) {
        this.img = pipeImage;
    }

    // Method to draw the wall
    public void drawWall(Graphics g) {
        g.drawImage(img, X, Y, null); // Draw lower wall
        g.drawImage(img, X, (-GamePanel.HEIGHT + (Y - gap)), null); // Draw upper wall
    }

    // Method to reset the wall position and set game over state
    private void wall_Reset(CoinImage coin, MagnetPowerUp magnetPower) {
        Y = r.nextInt(GamePanel.HEIGHT - 400) + 200; // Reset Y-coordinate of the wall
        height = GamePanel.HEIGHT - Y; // Reset height of the wall
        coin.setY(Y - (gap / 2)); // Adjust Y position of the coin
        magnetPower.setY(Y - (gap / 2)); // Adjust Y position of the magnet power-up
        GamePanel.GameOver = true; // Set game over state
        GamePanel.hasPassed = true; // Set hasPassed state
    }

    // Method to handle wall movement and interactions with other objects
    public void wallMovement(CoinImage coin, BirdTestAnimation bird, InvincibilityPower invPower, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        X += speed - (GamePanel.score / 4); // Move the wall to the left

        if (!hasPassed && X < bird.getX()) {
            hasPassed = true; // Mark the wall as passed when the bird goes beyond it
        }

        if (X <= -width_Wall) { // Reset wall position after it leaves the screen
            X = GamePanel.WIDTH;
            Y = r.nextInt(GamePanel.HEIGHT - 400) + 200; // Reset Y-coordinate
            height = GamePanel.HEIGHT - Y; // Reset height
            coin.setVisible(true); // Make the coin visible
            coin.setX(X + 15); // Adjust X position of the coin
            coin.setY(Y - (gap / 2)); // Adjust Y position of the coin
            GamePanel.score += 1; // Increment score
        }

        if (!InvincibilityPower.isInvincible()) { // Check if the bird is not invincible
            Rectangle lowerRect = new Rectangle(X, Y, width_Wall, height); // Rectangle for lower wall
            Rectangle upperRect = new Rectangle(X, 0, width_Wall, GamePanel.HEIGHT - (height + gap)); // Rectangle for upper wall

            if ((lowerRect.intersects(BirdTestAnimation.getBirdRect()) || upperRect.intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() <= 1) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score); // Send score to server if the user is logged in
                int option = GamePanel.popUpMessage(); // Display game over message

                if (option == 0) { // Player chooses to play again
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    BirdTestAnimation.reset(); // Reset the bird position
                } else if (option == 2) { // Player chooses to exit
                    JFrame frame = FlappyClass.getWindow();
                    MenuPanel.audioPlayer.stop(); // Stop music
                    frame.dispose(); // Close the game window
                    FlappyClass.timer.stop(); // Stop the game timer
                } else { // Go back to main menu
                    MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                    BirdTestAnimation.reset(); // Reset the bird position
                    FlappyClass.timer.stop(); // Stop the game timer
                    FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu"); // Show menu panel
                }
            } else if ((lowerRect.intersects(BirdTestAnimation.getBirdRect()) || upperRect.intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() > 1 && hit) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav"); // Play hurt sound

                hit = false; // Prevent further collision processing immediately
                if (collisionTimer == null || !collisionTimer.isRunning()) { // Check if the timer is not running
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            HeartsPowerUp.subHeart(); // Subtract a heart
                            hit = true; // Re-enable collision processing
                            InvincibilityPower.setFalse(); // Disable invincibility
                            collisionTimer = null; // Reset the timer reference
                        }
                    });
                    collisionTimer.setRepeats(false); // Ensure the timer only triggers once
                    collisionTimer.start(); // Start the timer
                }
            }
        }
    }
}
