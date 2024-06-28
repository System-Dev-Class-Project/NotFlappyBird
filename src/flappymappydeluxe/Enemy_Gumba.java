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
    
    private AudioPlayer audioPlayer;

    public Enemy_Gumba(BirdTestAnimation player, WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer=audioPlayer;
    	this.player = player;
        loadMagnetImage();
    }

    @Override
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        visible = b;
    }


    private void loadMagnetImage() {
        try {
            GuImg = ImageIO.read(new File("NotFlappyBird-main/Images/Gumba.png"));
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
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed())-speed; // Move the power-up at the same speed as the wall

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
        if (!InvincibilityPower.isInvincible()) {
            Rectangle enemyRect = getEnemypRect();
            Rectangle birdRect = BirdTestAnimation.getBirdRect();
    
            // Kollision mit einem Leben oder weniger
            if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() <= 1) {
                // Kollision mit Soundeffekten und Spielende
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
                GamePanel.sendScoreToServer(GamePanel.score); 
    
                // Pop-up Nachricht und entsprechende Aktionen
                int option = GamePanel.popUpMessage();
                if (option == 0) {  
                    try {
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    BirdTestAnimation.reset();
                } else if (option == 2) {    
                    JFrame frame = FlappyClass.getWindow();
                    MenuPanel.audioPlayer.stop();    
                    frame.dispose();
                    FlappyClass.timer.stop();
                } else {    
                    MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                    BirdTestAnimation.reset();
                    FlappyClass.timer.stop();
                    FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
                }
            } 
            // Kollision mit mehr als einem Leben
            else if (enemyRect.intersects(birdRect) && HeartsPowerUp.getHearts() > 1 && hit) {
                audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
                hit = false; 
                //System.out.println("Alien hit Bird");
                if (collisionTimer == null || !collisionTimer.isRunning()) { 
                    collisionTimer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) { 	
                            HeartsPowerUp.subHeart(); 
                            hit = true; 
                            InvincibilityPower.setFalse(); 
                            System.out.println("Heart lost to Gumba! Current hearts: " + HeartsPowerUp.getHearts());
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
            this.x = wall.X + 400;
            this.y = 800-(diameter)-25;
    }
}

    