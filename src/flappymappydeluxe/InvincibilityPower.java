package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * The InvincibilityPower class represents a power-up object in the game that grants temporary invincibility to the player.
 * This class implements the AttractableObject interface and includes methods for loading images, rendering, and moving the power-up.
 */
public class InvincibilityPower implements AttractableObject {
    private BufferedImage powerUpImg; // Image for the power-up
    public int x, y; // Position of the power-up
    private final int diameter = 25; // Size of the power-up
    private boolean visible = false; // Power-up visibility
    private static boolean invincible = false; // Invincibility status
    private long invincibleStartTime; // Time when invincibility starts
    private BirdTestAnimation player;  // The player object 
    public boolean setVisible;  // Set the visibility of the power-up

    private AudioPlayer audioPlayer; // Audio player for playing sound effects
    
    /**
     * Constructs an InvincibilityPower object.
     *
     * @param wall the WallImage object that influences the power-up's position.
     * @param audioPlayer the AudioPlayer object to play sounds when the power-up is collected.
     */
    public InvincibilityPower(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage();
    }

    /**
     * Loads the image for the invincibility power-up.
     */
    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("NotFlappyBird-main/Images/StarPowerUp.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the power-up on the screen.
     *
     * @param g the Graphics object used for drawing.
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
     * Sets the invincibility status.
     *
     * @param invincible the new invincibility status.
     */
    public void setInvincible(boolean invincible) {
        InvincibilityPower.invincible = invincible;
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
            invincible = false;
        }

        /**if (GamePanel.score % 1 == 0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        }*/

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/InvincibilityPowerUp_sound.wav");
            visible = false; // Make the power-up disappear
            invincible = true; // Make the player invincible
            bird.setRainbowColor(true);
            invincibleStartTime = System.currentTimeMillis();
        }

        // Check if invincibility should end
        if (invincible && (System.currentTimeMillis() - invincibleStartTime > 5000)) {
            invincible = false;
            bird.setRainbowColor(false);
        }
    }

    /**
     * Sets the visibility of the power-up.
     *
     * @param b true to make the power-up visible, false to hide it.
     */
    public void setVisible(boolean b) {
        visible = b;
    }

    /**
     * Sets the x-coordinate of the power-up.
     *
     * @param i the new x-coordinate of the power-up.
     */
    public void setX(int i) {
        x = i;
    }

    /**
     * Sets the y-coordinate of the power-up.
     *
     * @param i the new y-coordinate of the power-up.
     */
    public void setY(int i) {
        y = i;
    }

    /**
     * Checks if the player is currently invincible.
     *
     * @return true if the player is invincible, false otherwise.
     */
    public static boolean isInvincible() {
        return invincible;
    }

    /**
     * Sets the invincibility status to true.
     */
    public static void setTrue() {
        invincible = true;
    }

    /**
     * Sets the invincibility status to false.
     */
    public static void setFalse() {
        invincible = false;
    }

    /**
     * Moves the power-up to the player's position.
     *
     * @param x the x-coordinate of the player.
     * @param y the y-coordinate of the player.
     */
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the power-up relative to the player's position
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
     * Spawns the power-up at a specific location, usually near a wall.
     *
     * @param wall the wall image object which determines where the power-up will spawn.
     */
    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }
}
