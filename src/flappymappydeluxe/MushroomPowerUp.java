package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import javax.imageio.ImageIO;

// The MushroomPowerUp class represents a power-up in the game that the player can collect
public class MushroomPowerUp implements AttractableObject {

    private BufferedImage powerUpImg; // Image of the power-up
    private int x, y; // Position of the power-up
    private int diameter = 20; // Diameter of the power-up
    private static boolean visible = false; // Visibility of the power-up
    private long mushroomStartTime; // Timestamp when the power-up was collected
    public boolean setVisible; // Flag to set visibility
    private BirdTestAnimation player; // Reference to the player (bird) object
    Timer timer; // Timer for scheduling tasks

    private AudioPlayer audioPlayer; // Audio player for sound effects

    // Constructor for MushroomPowerUp
    public MushroomPowerUp(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage(); // Load the power-up image
    }

    // Method to load the power-up image
    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("NotFlappyBird-main/Images/Mushroom_.png"));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    // Method to draw the power-up on the screen
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(powerUpImg, x, y, null);
        }
    }

    // Method to check if the power-up is visible
    public boolean isVisible() {
        return visible;
    }

    // Method to get the bounding rectangle of the power-up
    public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Method to move the power-up along with the wall and handle interactions
    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / 4); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) { // If the game is over
            visible = false;
            bird.setMushroom(false);
        }

        // Check for collision with the bird
        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/ShrinkPowerUp_sound.wav"); // Play sound effect
            visible = false; // Make the power-up disappear
            bird.setMushroom(true); // Activate the mushroom power-up for the bird
            mushroomStartTime = System.currentTimeMillis(); // Record the time when the power-up was collected
        }

        // Deactivate the mushroom power-up after 5 seconds
        if (bird.isMushroom() && (System.currentTimeMillis() - mushroomStartTime > 5000)) {
            bird.setMushroom(false);
        }
    }

    // Method to move the power-up to the player's position
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the power-up relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    // Method to get the bounding rectangle of the power-up
    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Method to get the x-coordinate of the power-up
    @Override
    public int getX() {
        return x;
    }

    // Method to get the y-coordinate of the power-up
    @Override
    public int getY() {
        return y;
    }

    // Method to set the x-coordinate of the power-up
    @Override
    public void setX(int i) {
        this.x = i;
    }

    // Method to set the y-coordinate of the power-up
    @Override
    public void setY(int i) {
        this.y = i;
    }

    // Method to set the visibility of the power-up
    @Override
    public void setVisible(boolean b) {
        visible = b;
    }

    // Method to spawn the power-up at the given wall's position
    @Override
    public void spawn(WallImage wall) {
        visible = true;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }

    // Method to check if the mushroom power-up is active
    public static boolean isMushroom() {
        return visible;
    }
}
