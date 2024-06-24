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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy_Gumba implements Enemy{
    private static BufferedImage GuImg;
    public static int x;
    public static int y;
    private final int diameter = 200; // Size of the Gumba
    private static boolean visible = false; // Gumba visibility
    private BirdTestAnimation player;
    int jumpHeight = 250;
    int speed = 2;
    Random random = new Random();
    boolean isJumping = false;
    public boolean hit = true;
	private Timer collisionTimer = null;
    int groundLevel = 800-(diameter)-25; // Y-Position des Bodens
    int vy = 0; // Vertikale Geschwindigkeit
    int gravity = 1; // Beschleunigung (Schwerkraft)
    int jumpStartSpeed = -20; // Anfangsgeschwindigkeit des Sprungs (negativ für nach oben und regelt Höhe)

    public Enemy_Gumba(BirdTestAnimation player, WallImage wall) {
        this.player = player;
        this.x = wall.X+200;
        this.y = wall.Y - (WallImage.gap / 2);
        loadMagnetImage();
    }

    @Override
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        visible = b;
    }


    private void loadMagnetImage() {
        try {
            GuImg = ImageIO.read(new File("Images\\Gumba.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(GuImg, x, y, null);
        }
    }

    private void startJump() {
        if (!isJumping) {
            isJumping = true;
            vy = jumpStartSpeed; // Setze die Anfangsgeschwindigkeit für den Sprung
        }
    }


    public Rectangle getEnemypRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void moveEnemy(WallImage wall) {
        x += WallImage.speed - (GamePanel.score / 4)-speed; // Move the power-up at the same speed as the wall

        if (x < -diameter-1000) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
            y=groundLevel;
        }

        if (GamePanel.GameOver) {
            visible = false;
            isJumping = false;
        }
        
        if (isJumping) {
            y += vy; // Aktualisiere die Y-Position basierend auf der vertikalen Geschwindigkeit
            vy += gravity; // Simuliere Schwerkraft
    
            // Überprüfe, ob der Gumba den Boden erreicht hat
            if (y >= groundLevel) {
                y = groundLevel; // Verhindere, dass der Gumba durch den Boden fällt
                isJumping = false; // Beende den Sprung
                vy = 0; // Setze die vertikale Geschwindigkeit zurück
            }
        }
    
    // Stelle sicher, dass der Sprung irgendwo gestartet wird, z.B. durch eine Bedingung in der Update-Methode
    if (random.nextInt(100) > 97 && !isJumping) { // 5% Chance zu springen
        startJump();
    }
    
    /**if (GamePanel.score % 4==0) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 400;
        this.y = 800-(diameter)-25;
    }**/



    handleCollision();

        
    }

    public void handleCollision() {
        if (!InvincibilityPower.isInvincible() && visible) {
            if ((getEnemypRect().intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() <= 1) {
                boolean option = GamePanel.popUpMessage(); // Collision leads to game over if hearts are <= 1
                GamePanel.sendScoreToServer(GamePanel.score); // Send the score to the server (if the user is logged in
                
                if (option) { // Player chooses to play again
                    try {
                        Thread.sleep(500);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                    BirdTestAnimation.reset(); // Reset bird position
                    
                } else { // Player chooses not to play again
                    JFrame frame = FlappyClass.getWindow();
                    frame.dispose(); // Close the game window
                    FlappyClass.timer.stop();
                }
            } else if ((getEnemypRect().intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() > 1 && hit) {
                hit = false; // Prevent further collision processing immediately
                if (collisionTimer == null || !collisionTimer.isRunning()) { // Check if the timer is not running
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            HeartsPowerUp.subHeart(); // Subtract a heart
                            hit = true; // Re-enable collision processing after the delay
                            InvincibilityPower.setFalse(); // Disable invincibility
                            System.out.println("Heart lost to Gumba! Current hearts: " + HeartsPowerUp.getHearts());
                            collisionTimer = null; // Reset the timer reference to allow a new timer to be started
                        }
                    });
                    collisionTimer.setRepeats(false); // Ensure the timer only triggers once
                    collisionTimer.start(); // Start the timer
                }
            }
        }
    }

    @Override
    public void spawn(WallImage wall) {
        visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 400;
            this.y = 800-(diameter)-25;
    }
}

    