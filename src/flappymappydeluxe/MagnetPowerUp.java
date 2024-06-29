package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

// The MagnetPowerUp class represents a magnet power-up that attracts coins to the player when active.
public class MagnetPowerUp implements AttractableObject {
    private BufferedImage MagImg; // Image of the magnet power-up
    public int x, y; // Coordinates of the magnet power-up
    private final int diameter = 25; // Diameter of the magnet power-up
    private boolean visible = false; // Visibility of the magnet power-up
    private BirdTestAnimation player; // Reference to the player (bird)
    private long MagnetStartTime; // Start time for the magnet effect
    private int range = 100; // Range of the magnet effect
    private boolean active; // Whether the magnet is active
    private double attractionSpeed = 5; // Speed at which coins are attracted to the player
    private AudioPlayer audioPlayer; // Audio player for sound effects

    // Constructor to initialize the magnet power-up relative to the player
    public MagnetPowerUp(BirdTestAnimation player, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.player = player;
        this.active = false; // Initially, the magnet is not active
        loadMagnetImage(); // Load the magnet image
    }

    // Method to load the magnet image from file
    private void loadMagnetImage() {
        try {
            MagImg = ImageIO.read(new File("NotFlappyBird-main/Images/Magnet.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to draw the magnet power-up if it is visible
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(MagImg, x, y, null);
        }
    }

    // Set the magnet's active state
    public void setActive(boolean active) {
        this.active = active;
    }

    // Check if the magnet is active
    public boolean isActive() {
        return active;
    }

    // Get the bounding rectangle of the magnet power-up for collision detection
    public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Method to move the magnet power-up and handle its interactions
    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {

        x += WallImage.speed - (GamePanel.score / 4); // Move the power-up with the wall's speed

        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the power-up at the same speed as the wall


        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) { // Reset visibility and active state on game over
            visible = false;
            active = false;
        }

        // Collision detection with the player
        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/MagnetPowerUp_sound.wav"); // Play power-up collection sound
            visible = false; // Make the power-up disappear
            active = true; // Activate the magnet effect
            MagnetStartTime = System.currentTimeMillis(); // Record the start time of the magnet effect
        }

        // Deactivate the magnet effect after 5 seconds
        if (active && (System.currentTimeMillis() - MagnetStartTime > 5000)) {
            active = false;
        }
    }

    // Method to attract objects (e.g., coins) towards the player when the magnet is active
    public void attractObjects(List<AttractableObject> allObjects) {
        if (!active) return; // If the magnet is not active, do nothing

        // Define the area affected by the magnet
        Rectangle magnetArea = new Rectangle(
            player.getX() - range, 
            player.getY() - range, 
            player.getDiameter() + 2 * range, 
            player.getDiameter() + 2 * range
        );

        // Attract visible objects within the magnet's range towards the player
        for (AttractableObject obj : allObjects) {
            if (obj.isVisible() && magnetArea.intersects(obj.getRect())) {
                // Calculate direction towards the player
                int deltaX = player.getX() - obj.getX();
                int deltaY = player.getY() - obj.getY();

                // Normalize direction for consistent speed
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double normX = deltaX / distance;
                double normY = deltaY / distance;

                // Move the object slightly towards the player
                obj.setX(obj.getX() + (int) (normX * attractionSpeed));
                obj.setY(obj.getY() + (int) (normY * attractionSpeed));
            }
        }
    }

    // Method to move the magnet power-up to the player's position (not used here)
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the magnet relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    // Get the bounding rectangle of the magnet power-up
    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Get the x-coordinate of the magnet power-up
    @Override
    public int getX() {
        return x;
    }

    // Get the y-coordinate of the magnet power-up
    @Override
    public int getY() {
        return y;
    }

    // Set the x-coordinate of the magnet power-up
    @Override
    public void setX(int i) {
        this.x = i;
    }

    // Set the y-coordinate of the magnet power-up
    @Override
    public void setY(int i) {
        this.y = i;
    }

    // Set the visibility of the magnet power-up
    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    // Check if the magnet power-up is visible
    @Override
    public boolean isVisible() {
        return visible;
    }

    // Method to spawn the magnet power-up relative to a wall
    @Override
    public void spawn(WallImage wall) {
        visible = true; // Make the power-up visible
        // Set the power-up position relative to the wall
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }
}
