package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CoinImage {
    private BufferedImage coinImg;
    public int x, y;
    private final int diameter = 25; // Size of the coin
    private boolean visible = true; // Coin visibility
    private static int coinCount = 0; //Coin count

    public CoinImage(WallImage wall) {
        this.x = wall.X+10;
        this.y = wall.Y-(WallImage.gap/2);
        loadCoinImage();
    }

    private void loadCoinImage() {
        try {
            coinImg = ImageIO.read(new File("Images/Coin.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawCoin(Graphics g) {
        if (visible) {
            g.drawImage(coinImg, x, y, null);
        }
    }

    public Rectangle getCoinRect() {
        return new Rectangle(x, y, diameter, diameter);
    }
    
    public void moveCoin() {
        x += WallImage.speed; // Move the coin at the same speed as the wall
        
        Rectangle coin=  new Rectangle(x, y, diameter, diameter);
        if (x < -diameter) { // If the coin moves off-screen
            visible = false; // Make the coin invisible
        }
        
        if (GamePanel.GameOver) {
        	visible = true;
        	coinCount=0;
        }
        
        if (coin.intersects(BirdTestAnimation.getBirdRect()) && visible) {
            visible = false; // Make the coin disappear
            coinCount++; // Increase the coin count
            //TotalCoins+=coinCount;
        }
    }


    public boolean isVisible() {
        return visible;
    }

	public void setVisible(boolean b) {
		visible= b;
		// TODO Auto-generated method stub
		
	}

	public void setX(int i) {
		x=i;
		// TODO Auto-generated method stub
		
	}

	public int getDiameter() {
		// TODO Auto-generated method stub
		return diameter;
	}

	public void setY(int i) {
		y=i;
		// TODO Auto-generated method stub
		
	}

	public static String getCoinCount() {
		// TODO Auto-generated method stub

		return Integer.toString(coinCount);
	}
}
