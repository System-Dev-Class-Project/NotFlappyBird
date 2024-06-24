package flappymappydeluxe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import flappymappydeluxe.Button;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L; 
	
	private Button startButton;  // Initializing a button to start the game with
    private Button shopButton;  // Initializing a button to enter the shop with
	private Button settingsButton;  // Initializing a button to enter the settings with
    private BufferedImage img;  
    private BufferedImage img1;  
    private BufferedImage img2;
    public boolean StartingPoint = false;
    private int imgX = 0; // Initial x-coordinate of the image
    public static AudioPlayer audioPlayer; // Add an AudioPlayer instance
    private Timer visibilityCheckTimer;

    private CardLayout cardLayout;
    private JPanel mainPanel;
	
	
	
	public MenuPanel (CardLayout cardLayout, JPanel mainPanel, AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
		LoadImage();
		audioPlayer.play("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Plays the background music
		
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

	   
	    startButton = new Button(200, 225, 225, 125, "", "NotFlappyBird-main/Images/PlayButton.png");
        shopButton = new Button(210, 340, 175, 90, "", "NotFlappyBird-main/Images/ShopButton.png");
		settingsButton = new Button(220, 460, 150, 60, "", "NotFlappyBird-main/Images/SettingsButton.png");
        //this mouseListener stops the current track and switches to the new specified track whenever the panel is switched. The helper method switchMusic is defined below.
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (startButton.isClicked(e)) {
                	FlappyClass.restartGame();  // Restart the game
                    StartingPoint = true;
                    cardLayout.show(mainPanel, "game");
                    switchMusic("NotFlappyBird-main/Music/1-04.-Strike-the-Earth_-_Plains-of-Passage_.wav");
                    System.out.println("Play Button clicked");
                }
               
                if (shopButton.isClicked(e)) {
                    cardLayout.show(mainPanel, "shop");
                    switchMusic("NotFlappyBird-main/Music/Ace-Attorney-18-Marvin-Grossberg-_-Age_-Regret_-Retribution.wav");
                    System.out.println("Shop Button clicked");
                }
				if (settingsButton.isClicked(e)) {
					cardLayout.show(mainPanel, "settings");
					switchMusic("NotFlappyBird-main/Music/1-02.-Strike-the-Earth_-_Plains-of-Passage_.wav");
					System.out.println("Settings Button clicked");
				}
                
            }
        });
    }
	// Method to switch the currently playing music
    public static void switchMusic(String musicFile) {
        audioPlayer.stop();
        if (musicFile != null) {
            audioPlayer.play(musicFile);
        }
    }	
		
	
	//loads the menu panel image 
	private void LoadImage () {
		
		try {
			img = ImageIO.read(new File("NotFlappyBird-main/Images/pixelartbackground.png"));
			img1 = ImageIO.read(new File("NotFlappyBird-main/Images/CoinBig.png"));
			img2 = ImageIO.read(new File("NotFlappyBird-main/Images/NotFlappyBirdTitle.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//visualizes the loaded image in the entire menu panel, we use paintComponent and not paint because we visualize the main menu image as well as buttons
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, imgX, 0, null);
		g.drawImage(img2, 30 ,25, null);
		startButton.draw(g);
		shopButton.draw(g);
		settingsButton.draw(g);
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


	

	

