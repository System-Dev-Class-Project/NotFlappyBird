package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

/**
 * Represents a magnet power-up in the game that attracts nearby objects when active.
 */
public class MagnetPowerUp implements AttractableObject {
    private BufferedImage MagImg; // Image for the magnet power-up
    public int x, y; // Position of the magnet power-up
    private final int diameter = 25; // Size of the magnet power-up
    private boolean visible = false; // Magnet power-up visibility
    private BirdTestAnimation player; // Reference to the player
    private long MagnetStartTime; // Time when magnet effect starts
    private int range = 100; // Range of the magnet effect
    private boolean active; // Whether the magnet is active
    private double attractionSpeed = 5; // Speed at which objects are attracted
    private AudioPlayer audioPlayer; // Audio player for sound effects

    /**
     * Constructs a MagnetPowerUp object.
     *
     * @param player the player object
     * @param audioPlayer the audio player for sound effects
     */
    public MagnetPowerUp(BirdTestAnimation player, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.player = player;
        this.range = range; // Set the range of the magnet effect
        this.active = false; // Initially, the magnet is not active
        loadMagnetImage();
    }

    /**
     * Loads the image for the magnet power-up.
     */
    private void loadMagnetImage() {
        try {
            MagImg = ImageIO.read(new File("NotFlappyBird-main/Images/Magnet.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the magnet power-up on the screen.
     *
     * @param g the Graphics object used for drawing
     */
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(MagImg, x, y, null);
        }
    }

    /**
     * Sets the magnet's active status.
     *
     * @param active true to activate the magnet, false to deactivate
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the magnet is active.
     *
     * @return true if the magnet is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the bounding rectangle of the magnet power-up.
     *
     * @return the bounding rectangle of the magnet power-up
     */
    public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the magnet power-up along with the wall.
     *
     * @param wall the wall image
     * @param bird the player character
     */
    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = false;
            active = false;
        }

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/MagnetPowerUp_sound.wav");
            visible = false; // Make the power-up disappear
            active = true;
            MagnetStartTime = System.currentTimeMillis();
        }

        // Check if the magnet effect should end
        if (active && (System.currentTimeMillis() - MagnetStartTime > 5000)) {
            active = false;
        }
    }

    /**
     * Attracts nearby objects when the magnet is active.
     *
     * @param allObjects the list of all attractable objects in the game
     */
    public void attractObjects(List<AttractableObject> allObjects) {
        if (!active) return; // If the magnet is not active, do nothing

        Rectangle magnetArea = new Rectangle(player.getX() - range, player.getY() - range, player.getDiameter() + 2 * range, player.getDiameter() + 2 * range);

        for (AttractableObject obj : allObjects) {
            if (obj.isVisible() && magnetArea.intersects(obj.getRect())) {
                // Calculate direction towards the player
                int deltaX = player.getX() - obj.getX();
                int deltaY = player.getY() - obj.getY();

                // Normalize direction (optional, for consistent speed)
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double normX = deltaX / distance;
                double normY = deltaY / distance;

                // Set new position for the object, moving it slightly towards the player
                obj.setX(obj.getX() + (int) (normX * attractionSpeed)); // attractionSpeed defines how fast the object moves
                obj.setY(obj.getY() + (int) (normY * attractionSpeed));
            }
        }
    }

    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int i) {
        this.x = i;
    }

    @Override
    public void setY(int i) {
        this.y = i;
    }

    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }
}
