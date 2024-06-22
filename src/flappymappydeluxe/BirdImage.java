package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class BirdImage {
	
	private BufferedImage img = null;
	private static int bird_dia = 36;  //diameter needs to be 36 because of the counter
	public static int x = (GamePanel.WIDTH/2)- bird_dia/2;
	public static int y = GamePanel.HEIGHT/2;                    //the coordinates spawn the bird in the middle of the window
	
	private static int speed= 2;
	private int accel = 1;
	
	public BirdImage() {
		
		LoadImage();
		
	}
	
	private void LoadImage() {
		try {
			img = ImageIO.read(new File("Images/redbird-midflap.png"));
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void drawBird(Graphics g) { 
		g.drawImage(img, x, y,null);
	}
	
	public void birdMovement() {
		
		if(y>=0 && y<=GamePanel.HEIGHT) {
			speed+=accel;   //3, 4, 5
			y+=speed;    //400+3, 400+3+4, 400+3+4+5
		} else {
			boolean option = GamePanel.popUpMessage();
		
			if (option) { //whenever player hits out of bounds, the message will pop up as well
				try {
					Thread.sleep(500);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			reset();   
		} else {
			JFrame frame= FlappyClass.getWindow();
			frame.dispose(); //releases the native resources in the window, closing it
			FlappyClass.timer.stop();  //stops the timer so that when the player clicks no, the game stops
		}
		}
	}

	public static void reset() {  //public static because we need it in wallImage class
		speed = 2;
		y = GamePanel.HEIGHT/2;
		GamePanel.GameOver = true; //this factors in upper and lower out of bounds, resetting the bird to its original coordinates
		GamePanel.score =0; //resetting the score when gameover occurs
		
		
		
	}
	public void goUpwards() {
		speed=-17;
	}
	
	public static Rectangle getBirdRect() { //we need static, thats why all the variables here need to be static as well
		Rectangle birdRect = new Rectangle (x, y, bird_dia, 36);
		return birdRect;
	}
	
	
	
	
	
	
}
