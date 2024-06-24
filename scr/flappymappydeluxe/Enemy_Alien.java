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

public class Enemy_Alien implements Enemy{
    private static BufferedImage GuImg;
    public static int x;
    public static int y;
    private final int diameter = 100; // Size of the Gumba
    private static boolean visible = false; // Gumba visibility
    private BirdTestAnimation player;
    private int vy = 8; // Vertical velocity
    private int upperBound = 0; // Top of the screen
    private int lowerBound = 600; // Bottom of the screen, adjust this value based on your game's screen height
    public boolean hit = true;
	private Timer collisionTimer = null;
	
	private AudioPlayer audioPlayer;
    
    public Enemy_Alien(BirdTestAnimation player, WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer=audioPlayer;
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
            GuImg = ImageIO.read(new File("NotFlappyBird-main/Images/Alien.png"));
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
        x += WallImage.speed - (GamePanel.score / 4); // Move the power-up at the same speed as the wall

        if (x < -diameter-1000) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        y += vy;

        // Reverse direction if the power-up hits the top or bottom of the screen
        if (y <= upperBound || y + diameter >= lowerBound) {
            vy *= -1;
        }

        if (GamePanel.GameOver) {
            visible = false;
        }
        
        
        /**if (GamePanel.score % 4==0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 500-(diameter/2);
            this.y = wall.Y - (WallImage.gap / 2)-(diameter/2);
        }**/



        handleCollision();

        
    }

    public void handleCollision() {	
        if (!InvincibilityPower.isInvincible()) {
            if ((getEnemypRect().intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() <= 1) {
            	audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            	audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score); // Send the score to the server (if the user is logged in
            	int option = GamePanel.popUpMessage();

	             if (option == 0) {  //reset game if yes is clicked
	                 try {
	                     Thread.sleep(500);
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	                 BirdTestAnimation.reset();
	             } else if (option == 2) {    //close game if no is clicked
	             	 JFrame frame = FlappyClass.getWindow();
	                  MenuPanel.audioPlayer.stop();    //stops the music from playing when no is pressed after falling out of bounds. Also inserted in the wallImage class in the case of wall collision
	                  frame.dispose();
	                  FlappyClass.timer.stop();
	             }
	             else {    //last possible option is to go back to main menu 
	                 // Go back to main menu
	             	MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
	             	BirdTestAnimation.reset();
	             	FlappyClass.timer.stop();
	                 FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
	             }
	         }
            } else if ((getEnemypRect().intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() > 1 && hit) {
            	audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            	hit = false; // Prevent further collision processing immediately
                if (collisionTimer == null || !collisionTimer.isRunning()) { // Check if the timer is not running
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) { 	
                            HeartsPowerUp.subHeart(); // Subtract a heart
                            hit = true; // Re-enable collision processing after the delay
                            InvincibilityPower.setFalse(); // Disable invincibility
                            System.out.println("Heart lost to Alien! Current hearts: " + HeartsPowerUp.getHearts());
                            collisionTimer = null; // Reset the timer reference to allow a new timer to be started
                        }
                    });
                    collisionTimer.setRepeats(false); // Ensure the timer only triggers once
                    collisionTimer.start(); // Start the timer
                }
            }
        }
    

    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 500-(diameter/2);
        this.y = wall.Y - (WallImage.gap / 2)-(diameter/2);
    }
}

    
