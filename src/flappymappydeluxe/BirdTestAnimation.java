package flappymappydeluxe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Represents the bird animation in the game.
 */
public class BirdTestAnimation {

    private BufferedImage[] frames; // Array to hold bird frames
    private int currentFrameIndex; // Index of the current frame
    private int animationSpeed = 8; // Adjust this to control the speed of animation
    private int frameCounter = 0; // Counter to control animation speed

    private static int bird_dia = 36; // Diameter of the bird
    private static int mushroom_dia = 10; // Diameter of the mushroom
    private boolean mushroom = false; // Indicates if the bird has a mushroom
    public static int x = (GamePanel.WIDTH / 2) - bird_dia / 2; // X position of the bird
    public static int y = GamePanel.HEIGHT / 2; // Y position of the bird

    private AudioPlayer audioPlayer; // Audio player instance
    private static int speed = 2; // Vertical speed of the bird
    private int accel = 1; // Acceleration of the bird

    private boolean rainbowColor = false; // Variable for rainbow colors
    private int rainbowOffset = 0; // Offset for rainbow color animation

    /**
     * Constructor for BirdTestAnimation class.
     */
    public BirdTestAnimation() {
        LoadImages(mushroom); // Load bird images
        currentFrameIndex = 0;
        this.x = x;
        this.y = y;
    }

    /**
     * Loads bird images based on whether it has a mushroom or not.
     * @param mushroom Indicates if the bird has a mushroom
     */
    private void LoadImages(boolean mushroom) {
        this.mushroom = mushroom; // Set mushroom state
        frames = new BufferedImage[2]; // Initialize frames array
        String activeBirdSkin = ShopPanel.getActiveBirdSkinIdle(); // Get active bird skin from shop

        try {
            if (mushroom) {
                bird_dia = mushroom_dia; // Set smaller diameter for mushroom bird
                // Load smaller bird images based on active bird skin
                if (activeBirdSkin != null) {
                    switch (activeBirdSkin) {
                        case "Purple":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdFlap_small.png"));
                            break;
                        case "Blue":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdFlap_small.png"));
                            break;
                        case "The Original":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdFlap_small.png"));
                            break;
                        case "Original":
                        default:
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap-small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap-small.png"));
                            break;
                    }
                } else {
                    frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap-small.png"));
                    frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap-small.png"));
                }
            } else {
                bird_dia = 36; // Set normal diameter for regular bird
                // Load normal bird images based on active bird skin
                if (activeBirdSkin != null) {
                    switch (activeBirdSkin) {
                        case "Purple":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdFlap.png"));
                            break;
                        case "Blue":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdFlap.png"));
                            break;
                        case "The Original":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdFlap.png"));
                            break;
                        case "Original":
                        default:
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap.png"));
                            break;
                    }
                } else {
                    frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap.png"));
                    frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap.png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the bird animation.
     * @param g Graphics object used for drawing
     */
    public void drawBird(Graphics g) {
        if (InvincibilityPower.isInvincible() && rainbowColor) {
            drawRainbowBird(g); // Draw rainbow bird if invincible and rainbowColor is true
        } else {
            g.drawImage(frames[currentFrameIndex], x, y, null); // Draw normal bird frames
        }
    }

    /**
     * Draws the bird with rainbow colors.
     * @param g Graphics object used for drawing
     */
    private void drawRainbowBird(Graphics g) {
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA}; // Array of rainbow colors

        for (int i = 0; i < frames.length; i++) {
            Color color = colors[(i + rainbowOffset) % colors.length]; // Get current rainbow color
            g.setColor(color);
            g.drawImage(frames[i], x, y, null); // Draw rainbow bird frames
        }

        rainbowOffset = (rainbowOffset + 1) % colors.length; // Update rainbow offset for animation
    }

    /**
     * Sets the bird images.
     * @param birdImage1 Image for first frame of bird animation
     * @param birdImage2 Image for second frame of bird animation
     */
    public void setBirdImages(BufferedImage birdImage1, BufferedImage birdImage2) {
        frames[0] = birdImage1;
        frames[1] = birdImage2;
    }

    /**
     * Controls the movement of the bird.
     * @param audioPlayer Audio player instance used for playing sounds
     */
    public void birdMovement(AudioPlayer audioPlayer) {
        if (y >= 0 && y <= GamePanel.HEIGHT) {
            speed += accel; // Increase speed due to gravity
            y += speed; // Update bird's vertical position
        } else {
            // Game over conditions
            audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
            audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            GamePanel.sendScoreToServer(GamePanel.score); // Send score to server

            // Display game over message and handle options
            int option = GamePanel.popUpMessage();
            if (option == 0) {
                try {
                    Thread.sleep(500); // Wait before resetting
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                reset(); // Reset game
            } else if (option == 2) {
                JFrame frame = FlappyClass.getWindow();
                MenuPanel.audioPlayer.stop();
                frame.dispose(); // Dispose game window
                FlappyClass.timer.stop(); // Stop game timer
            } else {
                MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Switch music
                reset(); // Reset game
                FlappyClass.timer.stop(); // Stop game timer
                FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu"); // Switch to menu panel
            }
        }
        animate(); // Animate bird frames
    }

    /**
     * Moves the bird upwards.
     */
    public void goUpwards() {
        speed = -17; // Set speed for bird to go upwards
    }

    /**
     * Retrieves the bounding rectangle of the bird.
     * @return Rectangle representing the bird's bounds
     */
    public static Rectangle getBirdRect() {
        return new Rectangle(x, y, bird_dia, 35); // Return rectangle representing bird's bounds
    }

    /**
     * Animates the bird frames.
     */
    private void animate() {
        frameCounter++;
        if (frameCounter >= animationSpeed) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.length; // Update current frame index
            frameCounter = 0; // Reset frame counter
        }
    }

    /**
     * Resets the bird's position and game state.
     */
    public static void reset() {
        speed = 2; // Reset speed
        y = GamePanel.HEIGHT / 2; // Reset y position
        GamePanel.GameOver = true; // Set game over state
        GamePanel.score = 0; // Reset score
    }

    /**
     * Retrieves the x position of the bird.
     * @return Current x position of the bird
     */
    public int getX() {
        return x;
    }

    /**
     * Checks if the bird has a mushroom.
     * @return True if the bird has a mushroom, false otherwise
     */
    public boolean isMushroom() {
        return mushroom;
    }

    /**
     * Sets whether the bird has a mushroom.
     * @param b True to set the bird as having a mushroom, false otherwise
     */
    public void setMushroom(boolean b) {
        mushroom = b; // Set mushroom state
        LoadImages(mushroom); // Reload bird images based on mushroom state
    }

    /**
     * Retrieves the diameter of the bird.
     * @return Diameter of the bird
     */
    public int getDiameter() {
        return bird_dia;
    }

    /**
     * Retrieves the width of the bird.
     * @return Width of the bird
     */
    public int getWidth() {
        return 36;
    }

    /**
     * Retrieves the height of the bird.
     * @return Height of the bird
     */
    public int getHeight() {
        return 36;
    }

    /**
     * Retrieves the y position of the bird.
     * @return Current y position of the bird
     */
    public int getY() {
        return y;
    }

    /**
     * Sets whether the bird should be displayed with rainbow colors.
     * @param rainbowColor True to display bird with rainbow colors, false otherwise
     */
    public void setRainbowColor(boolean rainbowColor) {
        this.rainbowColor = rainbowColor;
    }
}
