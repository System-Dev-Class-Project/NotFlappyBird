package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class HeartsPowerUp {

    private static final int MAX_VISIBILITY_DURATION = 0;
    private BufferedImage heartImg;
    private int x, y;
    private int diameter = 20; // Diameter of the power-up
    private boolean visible = false;
    private int vy = 5; // Vertical velocity
    private int upperBound = 0; // Top of the screen
    private int lowerBound = 600; // Bottom of the screen, adjust this value based on your game's screen height
    private static int heart = 10;


    public HeartsPowerUp(WallImage wall) {
        this.x = wall.X + 500;
        this.y = wall.Y - (WallImage.gap / 2);
        this.heart=heart;
        loadPowerUpImage();
    }

    private void loadPowerUpImage() {
        try {
            heartImg = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\heart.png"));
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(heartImg, x, y, null);
        }

         // Draw the hearts
         for (int i = 0; i < heart; i++) {
            g.drawImage(heartImg, 10 + i * (heartImg.getWidth() + 10), 10, null); // Draw each heart 10 pixels apart
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

        y += vy;

        // Reverse direction if the power-up hits the top or bottom of the screen
        if (y <= upperBound || y + diameter >= lowerBound) {
            vy *= -1;
        }

        if (x < -diameter-1000) { // If the power-up moves off-screen
            visible = false; // Make the power-up invisible
        }

        if (GamePanel.GameOver) {
            visible = true;
            heart=1;
        }

        int visibilityDuration = 0;
        // Adjust the score condition for visibility and introduce a visibility duration
        if (GamePanel.score % 4 == 0 && visibilityDuration == 0) { // Less frequent visibility trigger
            visible = true;
            visibilityDuration = MAX_VISIBILITY_DURATION; // Reset visibility duration
            this.x = wall.X + 500;
            this.y = wall.Y - (WallImage.gap / 2);
        }

    // Decrement visibility duration if the heart is visible
    if (visible && (int) visibilityDuration > 0) {
        visibilityDuration--;
        if ((int) visibilityDuration <= 0) {
            visible = false; // Make the heart invisible after duration expires
        }
    }

    if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            visible = false; // Make the power-up disappear
            heart+=1; // Increase the bird's hearts by 1
            System.out.println("Heart collected! Current hearts: " + heart);
        }
    }

    public static int getHearts() {
        return heart;
    }

    public static void subHeart() {
        heart -= 1;
    }
}


