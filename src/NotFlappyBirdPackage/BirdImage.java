package NotFlappyBirdPackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class BirdImage {
		
	private BufferedImage img = null;
	private static int bird_dia=36; //score won´t work otherwise
	public static int x = (GamePanel.WIDTH/2)-bird_dia/2;
	public static int y = GamePanel.HEIGHT/2;
	
	private static int speed=2;
	private int acce= 1;
	
	
	public BirdImage() {
		
		LoadImage();
		
	}



	private void LoadImage() {
		
		try {
			img = ImageIO.read(new File("C:\\Users\\Kevin\\eclipse-workspace\\Not_Flappy_Bird\\Images\\Bird.png"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void drawBird(Graphics g) {
		
		g.drawImage(img, x, y, null);
		
	}
	
	public void birdMovement() {
		
		if(y>=0 && y<=GamePanel.HEIGHT) {
			speed+=acce;
			y+=speed;
			
		}else {
			
			boolean option = GamePanel.popUpMessage();
			
			if(option) {
				try {
					Thread.sleep(500);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				reset();
			}else {
				//close window
				JFrame frame = MainBird.getWindow();
				frame.dispose();
				MainBird.timer.stop();
			}
			}
			

		}
	
	
	public void goUpwards() {
		speed = -17;
		
	}
	
	public static void reset() {
		speed=2;
		y= GamePanel.HEIGHT/2;
		GamePanel.GameOver= true;
		GamePanel.score=0;
	}
	
	public static Rectangle getBirdRect() {
		Rectangle birdRect = new Rectangle(x,y,bird_dia, 35); //height+ width
		return birdRect;
		
		
	}

}
