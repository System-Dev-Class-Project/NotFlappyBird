package NotFlappyBirdPackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class WallImage {
	
	private Random r = new Random();
	public int X;
	public int Y= r.nextInt(GamePanel.HEIGHT-400)+200; //max 600, min 200
	private int width_Wall= 45;
	private int height= GamePanel.HEIGHT -Y;
	public static int gap = 200;
	
	public static int speed = -6;
	
	
	private BufferedImage img=null;
	
	public WallImage(int X) {
		this.X=X;
		
		LoadImage();
		
	}
	
	private void LoadImage() {
		
		try {
			img = ImageIO.read(new File("Images\\Wall.png"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void drawWall(Graphics g) {
		
		g.drawImage(img, X, Y, null); //bottom
		g.drawImage(img, X, (-GamePanel.HEIGHT)+(Y-gap), null); //upper
		
	}
	
	public void wallMovement(CoinImage coin) {
		
		X+=speed;//600 for 1 wall, 900 for 2. Wall => go to 0
		
		if(X<=width_Wall) {
			X = GamePanel.WIDTH;
			Y=r.nextInt(GamePanel.HEIGHT-400)+200;
			height= GamePanel.HEIGHT-Y;
			coin.setVisible(true);
			coin.setX(X); // Assuming you have a setX method in CoinImage
			coin.setY(Y - (gap/2)); // Adjust Y position if necessary
			
			// TODO Auto-generated method stub
			
		}
		
		Rectangle lowerRect= new Rectangle(X, Y, width_Wall, height);
		Rectangle upperRect = new Rectangle (X,0, width_Wall, GamePanel.HEIGHT-(height+gap));
		
		if(lowerRect.intersects(BirdImage.getBirdRect()) || upperRect.intersects(BirdImage.getBirdRect())) {
			boolean option = GamePanel.popUpMessage();
			
			if(option) {
				try {
					Thread.sleep(500);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				BirdImage.reset();
				wall_Reset(coin);
			}else {
				//close window
				JFrame frame = MainBird.getWindow();
				frame.dispose();
				MainBird.timer.stop();
			}
			}
			
			
		
	}

	private void wall_Reset(CoinImage coin) {
		
		Y=r.nextInt(GamePanel.HEIGHT-400)+200;
		height= GamePanel.HEIGHT -Y;
		coin.setY(Y - (gap/2)); // Adjust Y position if necessary
		GamePanel.GameOver= true;
		

		
	}

}
