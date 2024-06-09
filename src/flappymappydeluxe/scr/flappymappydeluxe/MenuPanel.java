package flappymappydeluxe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L; 
	
	private Button startButton;  // initializing a button to start the game with
	private BufferedImage img;  
	private BufferedImage img1;  
	private BufferedImage img2;
	public boolean StartingPoint = false;
	private int imgX = 0; // Initial x-coordinate of the image
	
	
	
	public MenuPanel () {
		LoadImage();
		Timer timer = new Timer(10, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            // Update background position
	            imgX += -2;

	            // Wrap background image to create continuous movement
	            if (imgX >= getWidth()) {
	                imgX = -img.getWidth(null);
	            }

	            // Repaint the panel
         repaint();
	            
	        }
	    });
	    timer.start();

	   
		startButton = new Button(200,200,150,50,"Play");
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (startButton.isClicked(e)) {
					StartingPoint=true;
					System.out.println("Play Button clicked");
				}
			  
			}
		});
		
		
	}
	//loads the menu panel image 
	private void LoadImage () {
		
		try {
			img = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\pixelartbackground.png"));
			img1 = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\CoinBig.png"));
			img2 = ImageIO.read(new File("src\\flappymappydeluxe\\Images\\NotFlappyBirdText.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//visualizes the loaded image in the entire menu panel, we use paintComponent and not paint because we visualize the main menu image as well as buttons
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, imgX, 0, null);
		g.drawImage(img2, 50 ,50, null);
		startButton.draw(g);
		//g.drawImage(img1, 75, 650, null);  coin 
		if (imgX + img.getWidth() < getWidth()) {
	        g.drawImage(img, imgX + img.getWidth(), 0, null);
	        
	        }
		if (imgX <= -img.getWidth()) {
	        imgX = 0; }
	        // Reset imgX to repeat the background image
	       
		
		 
	}
	//this concerns the menu panel background, we decided that it would look more lively if the background moved here as well.
			//for simplicity, we move the menu background with the same speed as the walls
	public void MenuMove() {
		imgX+=WallImage.speed;
		
		if (imgX ==-2048) {
			imgX=0;
		}
	}
	
	
}


	

	

