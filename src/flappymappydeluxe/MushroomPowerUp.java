package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import javax.imageio.ImageIO;

/**
 * Represents a mushroom power-up in the game.
 * Implements the AttractableObject interface.
 */
public class MushroomPowerUp implements AttractableObject {

    // Image representing the mushroom power-up
    private BufferedImage powerUpImg;
    // Coordinates of the power-up
    private int x, y;
    // Diameter of the power-up
    private int diameter = 20;
    // Visibility status of the power-up
    private static boolean visible = false;
    // Timestamp for when the mushroom power-up was collected
    private long mushroomStartTime;
    public boolean setVisible;
    // Reference to the player (bird)
    private BirdTestAnimation player;
    Timer timer;
    // Audio player for playing sound effects
    private AudioPlayer audioPlayer;

    /**
     * Constructor to initialize the MushroomPowerUp.
     *
     * @param wall The wall image associated with the power-up's initial position.
     * @param audioPlayer The audio player for sound effects.
     */
    public MushroomPowerUp(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage();
    }

    /**
     * Loads the image for the mushroom power-up from file.
     */
    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("NotFlappyBird-main/Images/Mushroom_.png"));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Draws the power-up image on the screen if it is visible.
     *
     * @param g The Graphics object for drawing.
     */
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(powerUpImg, x, y, null);
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
     * Gets the rectangular bounds of the power-up.
     *
     * @return The rectangle representing the power-up's bounds.
     */
    public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the power-up along with the wall and checks for collisions with the player.
     *
     * @param wall The wall image for position reference.
     * @param bird The player (bird) for collision detection.
     */
    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) { // If the game is over
            visible = false; // Make the power-up invisible
            bird.setMushroom(false); // Reset the mushroom state of the bird
        }

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/ShrinkPowerUp_sound.wav"); // Play the power-up sound
            visible = false; // Make the power-up disappear
            bird.setMushroom(true); // Activate the mushroom effect on the bird
            mushroomStartTime = System.currentTimeMillis(); // Record the time the power-up was collected
        }

        if (bird.isMushroom() && (System.currentTimeMillis() - mushroomStartTime > 5000)) {
            bird.setMushroom(false); // Deactivate the mushroom effect after 5 seconds
        }
    }

    /**
     * Moves the power-up towards the player's position.
     *
     * @param x The x-coordinate of the player.
     * @param y The y-coordinate of the player.
     */
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the power-up relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    /**
     * Gets the rectangular bounds of the power-up.
     *
     * @return The rectangle representing the power-up's bounds.
     */
    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Gets the x-coordinate of the power-up.
     *
     * @return The x-coordinate of the power-up.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the power-up.
     *
     * @return The y-coordinate of the power-up.
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Sets the x-coordinate of the power-up.
     *
     * @param i The new x-coordinate of the power-up.
     */
    @Override
    public void setX(int i) {
        this.x = i;
    }

    /**
     * Sets the y-coordinate of the power-up.
     *
     * @param i The new y-coordinate of the power-up.
     */
    @Override
    public void setY(int i) {
        this.y = i;
    }

    /**
     * Sets the visibility status of the power-up.
     *
     * @param b The new visibility status.
     */
    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    /**
     * Spawns the power-up at a specified position relative to the wall.
     *
     * @param wall The wall image for position reference.
     */
    @Override
    public void spawn(WallImage wall) {
        visible = true;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }

    /**
     * Checks if the mushroom power-up is currently active.
     *
     * @return true if the power-up is visible, false otherwise.
     */
    public static boolean isMushroom() {
        return visible;
    }
}
