package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Enemy_Alien implements Enemy {
    private static BufferedImage GuImg; // Image of the alien
    public static int x; // X-coordinate of the alien
    public static int y; // Y-coordinate of the alien
    private final int diameter = 100; // Size of the alien
    private static boolean visible = false; // Alien visibility
    private BirdTestAnimation player; // Player (bird) object
    private int vy = 8; // Vertical velocity
    private int upperBound = 0; // Top of the screen
    private int lowerBound = 600; // Bottom of the screen, adjust this value based on your game's screen height
    public boolean hit = true; // Indicates if the alien has been hit
    private Timer collisionTimer = null; // Timer for collision processing
    private AudioPlayer audioPlayer; // Audio player for sound effects

    // Constructor to initialize the alien
    public Enemy_Alien(BirdTestAnimation player, WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.player = player;
        loadMagnetImage(); // Load alien image
    }

    @Override
    public void setVisible(boolean b) {
        visible = b; // Set visibility
    }

    // Load alien image from file
    private void loadMagnetImage() {
        try {
            GuImg = ImageIO.read(new File("NotFlappyBird-main/Images/Alien.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Draw the alien
    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(GuImg, x, y, null);
        }
    }

    // Get the bounding rectangle of the alien
    public Rectangle getEnemypRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    // Move the alien
    public void moveEnemy(WallImage wall) {
        x += WallImage.speed - (GamePanel.score / 4); // Move the alien at the same speed as the wall

        if (x < -diameter - 1000) { // If the alien moves off-screen
            visible = false; // Make the alien invisible
        }

        y += vy; // Move vertically

        // Reverse direction if the alien hits the top or bottom of the screen
        if (y <= upperBound || y + diameter >= lowerBound) {
            vy *= -1;
        }

        if (GamePanel.GameOver) {
            visible = false; // Make the alien invisible if the game is over
        }

        handleCollision(); // Check for collisions
    }

    // Handle collision with the bird
    public void handleCollision() {
        if (!InvincibilityPower.isInvincible()) {
            Rectangle enemyRect = getEnemypRect();
            Rectangle birdRect = BirdTestAnimation.getBirdRect();

            // Collision with one life or less
            if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() <= 1) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score);

                int option = GamePanel.popUpMessage();
                if (option == 0) { // Restart game
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    BirdTestAnimation.reset();
                } else if (option == 2) { // Exit game
                    JFrame frame = FlappyClass.getWindow();
                    MenuPanel.audioPlayer.stop();
                    frame.dispose();
                    FlappyClass.timer.stop();
                } else { // Return to main menu
                    MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                    BirdTestAnimation.reset();
                    FlappyClass.timer.stop();
                    FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
                }
            }
            // Collision with more than one life
            else if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() > 1 && hit) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                hit = false;
                System.out.println("Alien hit Bird");
                if (collisionTimer == null || !collisionTimer.isRunning()) {
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            HeartsPowerUp.subHeart();
                            hit = true;
                            InvincibilityPower.setFalse();
                            System.out.println("Heart lost to Alien! Current hearts: " + HeartsPowerUp.getHearts());
                            collisionTimer = null;
                        }
                    });
                    collisionTimer.setRepeats(false);
                    collisionTimer.start();
                }
            }
        }
    }

    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 500 - (diameter / 2);
        this.y = wall.Y - (WallImage.gap / 2) - (diameter / 2);
    }
}
 