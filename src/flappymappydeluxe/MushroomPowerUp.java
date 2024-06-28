package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class MushroomPowerUp implements AttractableObject{

    private BufferedImage powerUpImg;
    private int x, y;
    private int diameter = 20; // Diameter of the power-up
    private static boolean visible = false;
    private long mushroomStartTime;
    public boolean setVisible;
    private BirdTestAnimation player;
    Timer timer;

    private AudioPlayer audioPlayer;

    public MushroomPowerUp(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer=audioPlayer;
    	this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage();
    }

    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("NotFlappyBird-main/Images/Mushroom_.png"));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }


    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(powerUpImg, x, y, null);
        }
    }

    public boolean isVisible() {
		return visible;
	}

	public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / DifficultyManagement.getSpeed()); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = false;
            bird.setMushroom(false);
        }
        
        
        /**if (GamePanel.score % 2 == 0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        }*/
        

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            audioPlayer.play("NotFlappyBird-main/Music/ShrinkPowerUp_sound.wav");
        	
        	visible = false; // Make the power-up disappear
            bird.setMushroom(true);
            //System.out.println("Mushroom PowerUp started");
            mushroomStartTime = System.currentTimeMillis();
        }
        

        //System.out.println("Time difference: " + (System.currentTimeMillis() - mushroomStartTime));

        if (bird.isMushroom() && (System.currentTimeMillis() - mushroomStartTime > 5000)) {
            bird.setMushroom(false);
            //System.out.println("Mushroom PowerUp ended");


        
    }  
    }

    @Override
    public void moveToPlayer(int x, int y) {
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's position
        this.y = player.getY() - (diameter / 2);
}

    @Override
    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int i) {
        this.x = i;
    }

    @Override
    public void setY(int i) {
       this.y = i;
    }

    @Override
    public void setVisible(boolean b) {
       visible = b;
    }

    @Override
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }

    public static boolean isMushroom() {
        return visible;
    }


  
}



