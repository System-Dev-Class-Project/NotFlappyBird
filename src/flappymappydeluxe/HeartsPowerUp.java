package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * The HeartsPowerUp class represents a power-up object in the game that provides additional lives (hearts) to the player.
 */
public class HeartsPowerUp implements AttractableObject {

    private BufferedImage heartImg; 
    private int x, y; // Position of the power-up
    private int diameter = 20; // Diameter of the power-up
    private boolean visible = false; // Visibility of the power-up
    private static int heart;
    private static int StartHearts = 5;
    private BirdTestAnimation player;
    private AudioPlayer audioPlayer;

    /**
     * Constructs a HeartsPowerUp object.
     *
     * @param wall the WallImage object that influences the power-up's position.
     * @param audioPlayer the AudioPlayer object to play sounds when the power-up is collected.
     */
    public HeartsPowerUp(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 500;
        this.y = wall.Y - (WallImage.gap / 2);
        this.heart = StartHearts;
        loadPowerUpImage();
    }

    /**
     * Loads the image for the heart power-up.
     */
    private void loadPowerUpImage() {
        try {
            heartImg = ImageIO.read(new File("NotFlappyBird-main/Images/heart.jpg"));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Draws the power-up and the hearts on the screen.
     *
     * @param g the Graphics object used for drawing.
     */
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(heartImg, x, y, null);
        }

        // Draw the hearts with a 10-pixel gap between them
        for (int i = 0; i < heart; i++) {
            g.drawImage(heartImg, 10 + i * (heartImg.getWidth() + 10), 10, null); // Draw each heart 10 pixels apart
        }
    }

    /**
     * Checks if the power-up is currently visible.
     *
     * @return true if the power-up is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Gets the bounding rectangle of the power-up.
     *
     * @return the bounding rectangle of the power-up.
     */
    public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the power-up according to the game mechanics.
     *
     * @param wall the WallImage object that influences the power-up's movement.
     * @param bird the BirdTestAnimation object representing the player's character.
     */
    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = true;
            heart = StartHearts;
        }


         /**if (GamePanel.score % 1 == 0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        }*/

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/Life_pickup.wav");
            visible = false; // Make the power-up disappear
            heart += 1; // Increase the bird's hearts by 1
            System.out.println("Heart collected! Current hearts: " + heart);
        }
    }

    /**
     * Gets the current number of hearts the player has.
     *
     * @return the current number of hearts.
     */
    public static int getHearts() {
        return heart;
    }

    /**
     * Decreases the number of hearts the player has by one.
     */
    public static void subHeart() {
        heart -= 1;
    }

    /**
     * Moves the power-up to the player's position.
     *
     * @param x the x-coordinate of the player.
     * @param y the y-coordinate of the player.
     */
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    /**
     * Gets the bounding rectangle of the power-up.
     *
     * @return the bounding rectangle of the power-up.
     */
    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Gets the x-coordinate of the power-up.
     *
     * @return the x-coordinate of the power-up.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the power-up.
     *
     * @return the y-coordinate of the power-up.
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the power-up.
     *
     * @param i the new x-coordinate of the power-up.
     */
    @Override
    public void setX(int i) {
        this.x = i;
    }

    /**
     * Sets the y-coordinate of the power-up.
     *
     * @param i the new y-coordinate of the power-up.
     */
    @Override
    public void setY(int i) {
        this.y = i;
    }

    /**
     * Sets the visibility of the power-up.
     *
     * @param b true to make the power-up visible, false to hide it.
     */
    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    /**
     * Spawns the power-up at a specific location, usually near a wall.
     *
     * @param wall the wall image object which determines where the power-up will spawn.
     */
    @Override
    public void spawn(WallImage wall) {
        visible = true;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }

    /**
     * Sets the starting number of hearts for the player.
     *
     * @param i the starting number of hearts.
     */
    public static void setStartHearts(int i) {
        StartHearts = i;
    }

    /**
     * Gets the starting number of hearts for the player.
     *
     * @return the starting number of hearts.
     */
    public static int getStartHearts() {
        return StartHearts;
    }
}
