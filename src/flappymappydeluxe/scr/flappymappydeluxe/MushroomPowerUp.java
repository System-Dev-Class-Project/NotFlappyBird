package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MushroomPowerUp {

    private BufferedImage powerUpImg;
    private int x, y;
    private int diameter = 20; // Diameter of the power-up
    private boolean visible = false;
    private long mushroomStartTime;
    public boolean setVisible;


    public MushroomPowerUp(WallImage wall) {
        this.x = wall.X + 10;
        this.y = wall.Y - (WallImage.gap / 2);
        loadPowerUpImage();
    }

    private void loadPowerUpImage() {
        try {
            powerUpImg = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\Mushroom_.png"));
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
        x += WallImage.speed - (GamePanel.score / 4); // Move the power-up at the same speed as the wall

        if (x < -diameter) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = false;
            bird.setMushroom(false);
        }
        
        
        if (GamePanel.score % 4 == 0 && GamePanel.score != 0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        }

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            visible = false; // Make the power-up disappear
            bird.setMushroom(true);
            mushroomStartTime =  System.currentTimeMillis();
        }

        // Check if invincibility should end
        if (bird.isMushroom()  && (System.currentTimeMillis() - mushroomStartTime > 5000)) {
            bird.setMushroom(false);
        }
        
    }


}

