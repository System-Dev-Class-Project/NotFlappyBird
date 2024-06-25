package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class WallImage {
    
    private Random r = new Random();
    public int X;
    public int Y = r.nextInt(GamePanel.HEIGHT - 400) + 200;    // max 600 min 200
    public int width_Wall = 55;
    private int height = GamePanel.HEIGHT - Y;
    public static int gap = 200;
    public boolean hasPassed = false;
    public boolean hit = true;
    private Timer collisionTimer = null;

    private AudioPlayer audioPlayer;

    public static int speed = -6; // public static to move the game background at the same speed

    private BufferedImage img = null;

    public WallImage(int X) {
        this.X = X;
        LoadImage();
    }

    private void LoadImage() {
        try {
            img = ImageIO.read(new File("NotFlappyBird-main/Images/pipe-greendoublefinal.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPipeImage(BufferedImage pipeImage) {
        this.img = pipeImage;
    }

    public void drawWall(Graphics g) {
        g.drawImage(img, X, Y, null);  // bottom wall
        g.drawImage(img, X, (-GamePanel.HEIGHT + (Y - gap)), null); // upper wall
    }

    // Resets the walls and sets game over to true
    private void wall_Reset(CoinImage coin, MagnetPowerUp magnetPower) {                         
        Y = r.nextInt(GamePanel.HEIGHT - 400) + 200;
        height = GamePanel.HEIGHT - Y;
        coin.setY(Y - (gap / 2)); // Adjust Y position if necessary
        magnetPower.setY(Y - (gap / 2)); // Adjust Y position if necessary
        GamePanel.GameOver = true;
        GamePanel.hasPassed = true;
    }

    public void wallMovement(CoinImage coin, BirdTestAnimation bird, InvincibilityPower invPower, AudioPlayer audioPlayer) {
        // Resetting the wall position after it leaves the screen on the left
        this.audioPlayer = audioPlayer;
        X += speed - (GamePanel.score / 4);

        if (!hasPassed && X < bird.getX()) {
            hasPassed = true;
        }

        if (X <= -width_Wall) {
            X = GamePanel.WIDTH;
            Y = r.nextInt(GamePanel.HEIGHT - 400) + 200; 
            height = GamePanel.HEIGHT - Y; 
            coin.setVisible(true);
            coin.setX(X + 15); // Adjust X coin position
            coin.setY(Y - (gap / 2)); // Adjust Y position 
            GamePanel.score += 1;
        }

        if (!InvincibilityPower.isInvincible()) {
            Rectangle lowerRect = new Rectangle(X, Y, width_Wall, height);
            Rectangle upperRect = new Rectangle(X, 0, width_Wall, GamePanel.HEIGHT - (height + gap));
            // We call the getBirdRect method which is why it needs to be static
            if ((lowerRect.intersects(BirdTestAnimation.getBirdRect()) || upperRect.intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() <= 1) {
            	audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            	audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score); // Send the score to the server (if the user is logged in
                int option = GamePanel.popUpMessage(); // Object collision, leading to game over whenever the bird hits the walls
                
                if (option == 0) { // When player clicks yes, meaning they want to play again
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    BirdTestAnimation.reset(); // Resets the bird to its initial starting coordinates
                } else if (option == 2) {
                    JFrame frame = FlappyClass.getWindow();
                    MenuPanel.audioPlayer.stop();  // Stops music when pressing no after wall collision
                    frame.dispose(); // Releases all of the native resources displayed in the window, essentially closing it
                    FlappyClass.timer.stop();
                } else {
                    // Go back to main menu
                    MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                    BirdTestAnimation.reset();
                    FlappyClass.timer.stop();
                    FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
                }
            } else if ((lowerRect.intersects(BirdTestAnimation.getBirdRect()) || upperRect.intersects(BirdTestAnimation.getBirdRect())) && HeartsPowerUp.getHearts() > 1 && hit) {
            	audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");  //also play the hurt sound when life is lost
            	hit = false; // Prevent further collision processing immediately
                if (collisionTimer == null || !collisionTimer.isRunning()) { // Check if the timer is not running
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            HeartsPowerUp.subHeart();
                            hit = true; // Re-enable collision processing after the delay
                            InvincibilityPower.setFalse();
                            System.out.println("Heart lost! Current hearts: " + HeartsPowerUp.getHearts());
                            collisionTimer = null; // Reset the timer reference to allow a new timer to be started
                        }
                    });
                    collisionTimer.setRepeats(false); // Ensure the timer only triggers once
                    collisionTimer.start(); // Start the timer
                }
            }
        }
    }
}
