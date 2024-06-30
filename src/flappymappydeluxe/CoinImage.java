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

/**
 * The CoinImage class represents coins that players can collect in the game.
 * The collected coins are saved and can be used in the shop.
 */
public class CoinImage implements AttractableObject {

    private static final String FILENAME = "coin_count.txt"; // File to save the total coin count
    private BufferedImage coinImg; // Image of the coin
    public int x, y; // Position of the coin
    private final int diameter = 25; // Size of the coin
    private boolean visible = true; // Coin visibility
    static int coinCount = 0; // Coin count in the current game session
    static int TotalCoins; // Total coins collected across all sessions
    private BirdTestAnimation player;
    private AudioPlayer audioPlayer;

    /**
     * Constructs a CoinImage associated with a wall, player, and audio player.
     *
     * @param wall the wall image to associate with the coin
     * @param player the player character
     * @param audioPlayer the audio player for sound effects
     */
    public CoinImage(WallImage wall, BirdTestAnimation player, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadCoinImage();
        TotalCoins = CoinImage.loadCoinCount(); // Load total coins collected from the file
        this.player = player;
    }

    /**
     * Loads the coin image from a file.
     */
    private void loadCoinImage() {
        try {
            coinImg = ImageIO.read(new File("NotFlappyBird-main/Images/Coin.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the coin on the screen if it is visible.
     *
     * @param g the Graphics object used for drawing
     */
    public void drawCoin(Graphics g) {
        if (visible && DifficultyManagement.powerupSpawnedForCurrentScore()) {
            g.drawImage(coinImg, x, y, null);
        }
    }

    /**
     * Returns a Rectangle representing the coin's bounding box for collision detection.
     *
     * @return the bounding rectangle of the coin
     */
    public Rectangle getCoinRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    /**
     * Moves the coin along with the wall and checks for collisions with the player.
     */
    public void moveCoin() {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the coin at the same speed as the wall
        
        Rectangle coin = new Rectangle(x, y, diameter, diameter);
        if (x < -diameter) { // If the coin moves off-screen
            visible = false; // Make the coin invisible
        }
        
        if (GamePanel.GameOver) {
            visible = true;
            coinCount = 0;
            saveCoinCount(TotalCoins);
        }
        
        /**
     * Checks if the coin intersects with the bird and performs actions if it does.
     * If the coin is visible and intersects with the bird, it plays a sound, makes the coin disappear,
     * increases the coin count, saves the total coins, and increments the total coins collected across games.
     *
     * @param coin the coin object to check for intersection
     * @param visible the visibility status of the coin
     * @param audioPlayer the audio player to play sounds
     * @param coinCount the current count of coins in the game session
     * @param TotalCoins the total count of coins collected across games
     */
    if (coin.intersects(BirdTestAnimation.getBirdRect()) && visible) {
        audioPlayer.play("NotFlappyBird-main/Music/coin_sound.wav");
        visible = false; // Make the coin disappear
        coinCount++; // Increase the coin count
        CoinImage.saveCoinCount(TotalCoins);
        TotalCoins += 1; // Increment total coins collected across games
    }
        }

    /**
     * Checks if the coin is visible.
     *
     * @return true if the coin is visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the coin.
     *
     * @param b the new visibility state
     */
    public void setVisible(boolean b) {
        visible = b;
    }

    /**
     * Sets the x-coordinate of the coin.
     *
     * @param i the new x-coordinate
     */
    public void setX(int i) {
        x = i;
    }

    /**
     * Gets the diameter of the coin.
     *
     * @return the diameter of the coin
     */
    public int getDiameter() {
        return diameter;
    }

    /**
     * Gets the x-coordinate of the coin.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the coin.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the coin.
     *
     * @param i the new y-coordinate
     */
    public void setY(int i) {
        y = i;
    }

    /**
     * Gets the current coin count as a string.
     *
     * @return the current coin count
     */
    public static String getCoinCount() {
        return Integer.toString(coinCount);
    }

    /**
     * Loads the total coin count from a file.
     *
     * @return the total coin count
     */
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

    /**
     * Saves the total coin count to a file.
     *
     * @param coinCount the total coin count to save
     */
    public static void saveCoinCount(int coinCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            writer.write(Integer.toString(coinCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the bounding rectangle of the coin.
     *
     * @return the bounding rectangle
     */
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's position
        this.y = player.getY() - (diameter / 2);
    }

    @Override
    public void spawn(WallImage wall) {
        throw new UnsupportedOperationException("Unimplemented method 'spawn'");
    }

    @Override
    public void drawPowerUp(Graphics g) {
        throw new UnsupportedOperationException("Unimplemented method 'drawPowerUp'");
    }

    @Override
    public void movePowerUp(WallImage wi, BirdTestAnimation bi) {
        throw new UnsupportedOperationException("Unimplemented method 'movePowerUp'");
    }
}
