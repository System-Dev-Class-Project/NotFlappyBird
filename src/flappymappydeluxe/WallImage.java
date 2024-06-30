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

/**
 * The WallImage class represents the walls that the player must navigate through in the game.
 * It manages the movement, drawing, and collision detection of the walls.
 */
public class WallImage {

    private Random r = new Random(); // Random object for generating random gap
    public int X; // X position of the wall
    public int Y = r.nextInt(GamePanel.HEIGHT - 400)+200;// max 600 min 200, the vertical position of the lower wall's top edge.
    public int width_Wall = 55; // Width of the walls
    private int height = GamePanel.HEIGHT - Y; // Height of the wall of the lower wall
    public static int gap = 200; // Gap between the walls
    public boolean  hasPassed = false; // Flag to check if the first wall has been passed
    private Timer collisionTimer = null; // Timer for collision handling
    private AudioPlayer audioPlayer; // Audio player for playing sound effects
    public static int speed = -6; // speed of the game
    private BufferedImage img = null; // Image of the wall

    /**
     * Constructs a WallImage at the specified X position.
     *
     * @param X the X coordinate of the wall
     */
    public WallImage(int X) {
        this.X = X;
        LoadImage();
    }

    /**
     * Loads the wall image from a file.
     */
    private void LoadImage() {
        try {
            img = ImageIO.read(new File("NotFlappyBird-main/Images/pipe-greendoublefinal.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets the image to be used for the wall.
     *
     * @param pipeImage the BufferedImage to set as the wall image
     */
    public void setPipeImage(BufferedImage pipeImage) {
        this.img = pipeImage;
    }

    /**
     * Draws the wall on the screen.
     *
     * @param g the Graphics object used for drawing
     */
    public void drawWall(Graphics g) {
        g.drawImage(img, X, Y, null);  // bottom wall
        g.drawImage(img, X, (-GamePanel.HEIGHT + (Y - gap)), null); // upper wall
    }

    
    /**
     * Manages the movement of the wall and checks for collisions with the player.
     *
     * @param coin the CoinImage object to be moved with the wall
     * @param bird the BirdTestAnimation object representing the player's character
     * @param invPower the InvincibilityPower object for checking invincibility status
     * @param audioPlayer the AudioPlayer object for playing sound effects
     */
    public void wallMovement(CoinImage coin, BirdTestAnimation bird, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        X += speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Increase speed based on score

        if (!hasPassed && X < bird.getX()) { // Check if the first wall has been passed, important for the first score
            hasPassed = true;
        }

        if (X <= -width_Wall) {
            X = GamePanel.WIDTH;
            Y = r.nextInt(GamePanel.HEIGHT - 400) + 200;
            height = GamePanel.HEIGHT - Y;
            coin.setVisible(true);
            coin.setX(X + 15); // Adjust X coin position
            coin.setY(Y - (gap / 2)); // Adjust Y position
            GamePanel.score += 1;
        }

        handleCollision();
    }

    /**
     * Handles the collision detection between the wall and the bird.
     */
    private void handleCollision() {
        if (!InvincibilityPower.isInvincible()) {
            Rectangle lowerRect = new Rectangle(X, Y, width_Wall, height); // Rectangle representing the lower wall
            Rectangle upperRect = new Rectangle(X, 0, width_Wall, GamePanel.HEIGHT - (height + gap)); // Rectangle representing the upper wall
            Rectangle birdRect = BirdTestAnimation.getBirdRect();

            // Collision detection with one or fewer hearts
            if ((lowerRect.intersects(birdRect) || upperRect.intersects(birdRect)) && HeartsPowerUp.getHearts() <= 1) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score);

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
            else if ((lowerRect.intersects(birdRect) || upperRect.intersects(birdRect)) && HeartsPowerUp.getHearts() > 1) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                if (collisionTimer == null || !collisionTimer.isRunning()) {
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            HeartsPowerUp.subHeart();
                            System.out.println("Heart lost! Current hearts: " + HeartsPowerUp.getHearts());
                            collisionTimer = null; // Reset the timer
                        }
                    });
                    collisionTimer.setRepeats(false); // Set the timer to run once
                    collisionTimer.start(); // Start the timer
                }
            }
        }
    }
}
