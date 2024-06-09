package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class MagnetPowerUp {
    private BufferedImage MagImg;
    public int x, y;
    private final int diameter = 25; // Size of the coin
    private boolean visible = false; // Coin visibility
    private BirdTestAnimation player;
    private long MagnetStartTime;
    private int range=100; // The range of the magnet effect
    private boolean active; // Whether the magnet is active
    private double attractionSpeed= 5;

    public MagnetPowerUp(BirdTestAnimation player) {
        this.player = player;
        this.range = range;
        this.active = false; // Initially, the magnet is not active
        loadMagnetImage();
    }


    private void loadMagnetImage() {
        try {
            MagImg = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\Magent.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawPowerUp(Graphics g) {
        if (visible) {
            g.drawImage(MagImg, x, y, null);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
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
            active = false;
        }
        
        
        if (GamePanel.score % 4 == 0) {
            visible = true;
            // Set the power-up position relative to the wall
            this.x = wall.X + 10;
            this.y = wall.Y - (WallImage.gap / 2);
        }

        if (getPowerUpRect().intersects(BirdTestAnimation.getBirdRect()) && visible) {
            visible = false; // Make the power-up disappear
            active = true;
            MagnetStartTime =  System.currentTimeMillis();
        }

        // Check if invincibility should end
        if (active  && (System.currentTimeMillis() - MagnetStartTime > 5000)) {
            active=false;
        }
        }

    public void attractObjects(List<CoinImage> coins) {
        if (!active) return; // If the magnet is not active, do nothing

        // Combine all attractable objects into one list for easier processing
        List<AttractableObject> allObjects = new ArrayList<>();
        allObjects.addAll(coins);

        Rectangle magnetArea = new Rectangle(player.getX() - range, player.getY() - range, player.getDiameter() + 2 * range, player.getDiameter() + 2 * range);

        for (AttractableObject obj : allObjects) {
            if (obj.isVisible() && magnetArea.intersects(obj.getRect())) {
                // Calculate direction towards the player
                int deltaX = (int) player.getX() - obj.getX();
                int deltaY = (int) player.getY() - obj.getY();
    
                // Normalize direction (optional, for consistent speed)
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double normX = deltaX / distance;
                double normY = deltaY / distance;
    
                // Set new position for the object, moving it slightly towards the player
                obj.setX(obj.getX() + (int) (normX * attractionSpeed)); // attractionSpeed defines how fast the object moves
                obj.setY(obj.getY() + (int) (normY * attractionSpeed));
            }
        }
    }


    public void setY(int i) {
        // TODO Auto-generated method stub
        y=i;
    }
}
    