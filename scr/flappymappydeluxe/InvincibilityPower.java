package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class InvincibilityPower implements AttractableObject{
    private BufferedImage powerUpImg;
    public int x, y;
    private final int diameter = 25; // Size of the power-up
    private boolean visible = false; // Power-up visibility
    private static boolean invincible = false; // Invincibility status
    private long invincibleStartTime;
    private WallImage wall;
    private BirdTestAnimation player;  
    public boolean setVisible;
    private static int wallCounter = 0;

    private AudioPlayer audioPlayer;
    
    public InvincibilityPower(WallImage wall, AudioPlayer audioPlayer) {
        this.audioPlayer=audioPlayer;
    	this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage();
    }

    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("NotFlappyBird-main/Images/InvincibilityPower.png"));
        } catch (Exception e) {
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

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	public Rectangle getPowerUpRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void movePowerUp(WallImage wall, BirdTestAnimation bird) {
        x += WallImage.speed - (GamePanel.score / 4); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = true;
            invincible = false;
        }
        
        
        /**if (GamePanel.score % 2==0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        } */

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
        	audioPlayer.play("NotFlappyBird-main/Music/InvincibilityPowerUp_sound.wav");
        	visible = false; // Make the power-up disappear
            invincible = true;
            invincibleStartTime = System.currentTimeMillis();
        }

        // Check if invincibility should end
        if (invincible && (System.currentTimeMillis() - invincibleStartTime > 5000)) {
            invincible = false;
        }
        
    }


    public void setVisible(boolean b) {
        visible = b;
    }

    public void setX(int i) {
        x = i;
    }

    public void setY(int i) {
        y = i;
    }

    public static boolean isInvincible() {
        // TODO Auto-generated method stub
        return invincible;
    }

    public static void setTrue() {
        // TODO Auto-generated method stub
        invincible = true;
    }

    public static void setFalse() {
        // TODO Auto-generated method stub
        invincible = false;
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
    public void spawn(WallImage wall) {
        visible = true;
        // Set the power-up position relative to the wall
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
    }


}
    




