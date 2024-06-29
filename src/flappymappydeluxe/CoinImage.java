package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

// The CoinImage class represents coins that the player can collect to buy things in the shop.
// Coins collected across multiple rounds are saved.
public class CoinImage implements AttractableObject {

    private static final String FILENAME = "coin_count.txt"; // File to save the total coin count
    private BufferedImage coinImg; // Image of the coin
    public int x, y; // Coordinates of the coin
    private final int diameter = 25; // Diameter of the coin
    private boolean visible = true; // Visibility of the coin
    static int coinCount = 0; // Count of coins collected in the current game
    static int TotalCoins; // Total coins collected across all games
    private BirdTestAnimation player; // Reference to the player (bird)
    
    private AudioPlayer audioPlayer; // Audio player for sound effects

    // Constructor to initialize the coin relative to a wall and player
    public CoinImage(WallImage wall, BirdTestAnimation player, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadCoinImage(); // Load the coin image
        TotalCoins = CoinImage.loadCoinCount(); // Load total coins collected across games
        this.player = player;
    }

    // Method to load the coin image from file
    private void loadCoinImage() {
        try {
            coinImg = ImageIO.read(new File("NotFlappyBird-main/Images/Coin.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to draw the coin if it is visible and should be spawned at the current score
    public void drawCoin(Graphics g) {
        if (visible && DifficultyManagement.powerupSpawnedForCurrentScore()) {
            g.drawImage(coinImg, x, y, null);
        }
    }

    // Method to get the bounding rectangle of the coin for collision detection
    public Rectangle getCoinRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Method to move the coin and handle its interactions
    public void moveCoin() {

        x += WallImage.speed - (GamePanel.score / 4); // Move the coin with the wall's speed

        x += WallImage.speed-(GamePanel.score/DifficultyManagement.getSpeed()); // Move the coin at the same speed as the wall

        
        Rectangle coin = new Rectangle(x, y, diameter, diameter);
        if (x < -diameter) { // If the coin moves off-screen
            visible = false; // Make the coin invisible
        }
        
        if (GamePanel.GameOver) { // Reset visibility and coin count on game over
            visible = true;
            coinCount = 0;
            saveCoinCount(TotalCoins);
        }
        
        if (coin.intersects(BirdTestAnimation.getBirdRect()) && visible) { // Check for collision with the player
            audioPlayer.play("NotFlappyBird-main/Music/coin_sound.wav"); // Play coin collection sound
            visible = false; // Make the coin disappear
            coinCount++; // Increase the current game's coin count
            CoinImage.saveCoinCount(TotalCoins); // Save the total coin count
            TotalCoins += 1; // Update the total coins collected across games
        }
    }

    // Check if the coin is visible
    public boolean isVisible() {
        return visible;
    }

    // Set the visibility of the coin
    public void setVisible(boolean b) {
        visible = b;
    }

    // Set the x-coordinate of the coin
    public void setX(int i) {
        x = i;
    }

    // Get the diameter of the coin
    public int getDiameter() {
        return diameter;
    }

    // Get the x-coordinate of the coin
    public int getX() {
        return x;
    }   

    // Get the y-coordinate of the coin
    public int getY() {
        return y;
    }   

    // Set the y-coordinate of the coin
    public void setY(int i) {
        y = i;
    }

    // Get the current game's coin count as a string
    public static String getCoinCount() {
        return Integer.toString(coinCount);
    }

    // Method to load the total coin count from a file
    public static int loadCoinCount() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Default to 0 if file doesn't exist or error occurs
    }

    // Method to save the total coin count to a file
    public static void saveCoinCount(int coinCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            writer.write(Integer.toString(coinCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get the bounding rectangle of the coin
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Move the coin to the player's position
    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's x-coordinate
        this.y = player.getY() - (diameter / 2); // Center the coin relative to the player's y-coordinate
    }

    // Placeholder for the spawn method (not implemented)
    @Override
    public void spawn(WallImage wall) {
        throw new UnsupportedOperationException("Unimplemented method 'spawn'");
    }

    // Placeholder for the drawPowerUp method (not implemented)
    @Override
    public void drawPowerUp(Graphics g) {
        throw new UnsupportedOperationException("Unimplemented method 'drawPowerUp'");
    }

    // Placeholder for the movePowerUp method (not implemented)
    @Override
    public void movePowerUp(WallImage wi, BirdTestAnimation bi) {
        throw new UnsupportedOperationException("Unimplemented method 'movePowerUp'");
    }
}
