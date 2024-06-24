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

public class Enemy_Batman implements Enemy{
    private static BufferedImage GuImg;
    public static int x;
    public static int y;
    private final int diameter = 100; // Size of the Gumba
    private static boolean visible = false; // Gumba visibility
    private BirdTestAnimation player;
    int speed = 4;
    int attractionSpeed = 2;
    public boolean hit = true;
	private Timer collisionTimer = null;
    Random rand = new Random();
    
    public Enemy_Batman(BirdTestAnimation player, WallImage wall) {
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
            GuImg = ImageIO.read(new File("Images\\Fireball.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(GuImg, x, y, null);
        }
    }


    public Rectangle getEnemypRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void moveEnemy(WallImage wall) {
        x += WallImage.speed - (GamePanel.score / 4)-speed; // Move the power-up at the same speed as the wall

        if (x < -diameter-1000) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = false;
        }
        
        
        /**if (GamePanel.score % 4==0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 300;
            this.y = rand.nextInt(601); 
        }
        **/
        if (this.x > player.getX()) {
            if (this.x > player.getX()) {
                this.x -= attractionSpeed;
            } else if (this.x < player.getX()) {
                this.x += attractionSpeed;
            }
        }

        if (this.y > player.getY()) {
            this.y -= attractionSpeed;
        } else if (this.y < player.getY()) {
            this.y += attractionSpeed;
        }
        



        handleCollision();

        
    }

    public void handleCollision() {
        if (!InvincibilityPower.isInvincible() && visible) {
            if ((getEnemypRect().intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() <= 1) {
                GamePanel.sendScoreToServer(GamePanel.score); // Send the score to the server (if the user is logged in
                boolean option = GamePanel.popUpMessage(); // Collision leads to game over if hearts are <= 1
                
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
                            System.out.println("Heart lost to Batman! Current hearts: " + HeartsPowerUp.getHearts());
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
        this.x = wall.X + 300;
        this.y = rand.nextInt(601); 
    }
}

    

