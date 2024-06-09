package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
// the coinImaage class introduces coins that the player can collect in order to buy things in the shop. We save the coins collected in total so that the player can gather many coins over 
public class CoinImage implements AttractableObject{
	
	private static final String FILENAME = "coin_count.txt"; //saves the coin count for every game
    private BufferedImage coinImg;
    public int x, y;
    private final int diameter = 25; // Size of the coin
    private boolean visible = true; // Coin visibility
    static int coinCount = 0; //Coin count
    static int TotalCoins;
    private BirdTestAnimation player;

    public CoinImage(WallImage wall, BirdTestAnimation player) {
        this.x = wall.X+10;
        this.y = wall.Y-(WallImage.gap/2);
        loadCoinImage();
        TotalCoins = CoinImage.loadCoinCount(); //we want to save all the coins the player collected in total across multiple rounds
        this.player=player;
    }

    private void loadCoinImage() {
        try {
            coinImg = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\Coin.png"));
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
        x += WallImage.speed-(GamePanel.score/4); // Move the coin at the same speed as the wall
        
        Rectangle coin=  new Rectangle(x, y, diameter, diameter);
        if (x < -diameter) { // If the coin moves off-screen
            visible = false; // Make the coin invisible
        }
        
        if (GamePanel.GameOver) {
        	visible = true;
        	coinCount=0;
        	saveCoinCount(TotalCoins);
        }
        
        if (coin.intersects(BirdTestAnimation.getBirdRect()) && visible) {
            visible = false; // Make the coin disappear
            coinCount++; // Increase the coin count
            CoinImage.saveCoinCount(TotalCoins);
            TotalCoins+=1; // we save the amount of coins collected in total across games
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

    public int getX() {
        return x;
    }   

    public int getY() {
        return y;
    }   
	public void setY(int i) {
		y=i;
		// TODO Auto-generated method stub
		
	}

	public static String getCoinCount() {
		// TODO Auto-generated method stub

		return Integer.toString(coinCount);
	}
	//this method gets the saved coin count from the txt file if it exists
	public static int loadCoinCount() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
        return 0; // Default to 0 if file doesn't exist or error occurs
    }
	//this method saves the coin count 
	public static void saveCoinCount(int coinCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            writer.write(Integer.toString(coinCount));
        } catch (IOException e) {
            // Handle file writing errors
            e.printStackTrace();
        }
    }

    public Rectangle getRect() {
        // This method returns the bounding rectangle of the coin,
        // which is useful for collision detection.
        return new Rectangle(x, y, diameter, diameter);
    }

    @Override
    public void moveToPlayer(int x, int y) {
    // Assuming the player's position is given by player.getX() and player.getY(),
    // and the coin should move towards the player.
    // This method will adjust the coin's position to move towards the player.
    // The movement logic can be adjusted based on game requirements.
    // Here, we simply move the coin directly towards the player's position.
    // This could be enhanced with more complex logic for smoother movement.
        this.x = player.getX() - (diameter / 2); // Center the coin relative to the player's position
        this.y = player.getY() - (diameter / 2);
}
}
