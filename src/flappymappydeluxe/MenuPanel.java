package flappymappydeluxe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;


public class MenuPanel extends JPanel {
    private static final long serialVersionUID = 1L; 
    
    private Button startButton;  // Initializing a button to start the game with
    private Button shopButton;  // Initializing a button to enter the shop with
    private Button settingsButton;  // Initializing a button to enter the settings with
    private BufferedImage img;  //the menu background image
    private BufferedImage img1;  //main menu game title image
    public boolean StartingPoint = false;  //sets the game time to false every time the MenuPanel is opened so the game doesn't keep going in the background
    private int imgX = 0; // Initial x-coordinate of the image
    public static AudioPlayer audioPlayer; // Add an AudioPlayer instance
    
    private CardLayout cardLayout;    //MenuPanel added to the cardLayout mainPanel
    private JPanel mainPanel;
    
    public MenuPanel(CardLayout cardLayout, JPanel mainPanel, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;  //local audioPlayer instance to handle background themes
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        LoadImage();
        audioPlayer.play("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Plays the background music
        
        // Moves the background image in an appropriate timeframe
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update background position
                imgX -= 2;

                // Wrap background image to create continuous movement
                if (imgX <= -img.getWidth()) {
                    imgX = 0;
                }

                // Repaint the panel
                repaint();
            }
        });
        timer.start();

        startButton = new Button(200, 225, 225, 125, "", "NotFlappyBird-main/Images/PlayButton.png");
        shopButton = new Button(210, 340, 175, 90, "", "NotFlappyBird-main/Images/ShopButton.png");
        settingsButton = new Button(215, 440, 165, 75, "", "NotFlappyBird-main/Images/SettingsButton.png");
        // This mouseListener stops the current track and switches to the new specified track whenever the panel is switched. The helper method switchMusic is defined below.
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (startButton.isClicked(e)) {
                    FlappyClass.restartGame();  // Restart the game
                    StartingPoint = true; //we set startingPoint to true so that the game time resumes
                    cardLayout.show(mainPanel, "game"); //swap to game panel
                    switchMusic("NotFlappyBird-main/Music/1-04.-Strike-the-Earth_-_Plains-of-Passage_.wav"); //music swap to game music theme
                    System.out.println("Play Button clicked"); //print response to confirm
                }
               //same procedure for the other action listeners minus the game time start
                if (shopButton.isClicked(e)) {
                    cardLayout.show(mainPanel, "shop");
                    switchMusic("NotFlappyBird-main/Music/Ace-Attorney-18-Marvin-Grossberg-_-Age_-Regret_-Retribution.wav");
                    System.out.println("Shop Button clicked");
                }
                if (settingsButton.isClicked(e)) {
                    cardLayout.show(mainPanel, "settings");
                    audioPlayer.stop();
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
        
    // Loads the menu panel image 
    private void LoadImage() {
        try {
            img = ImageIO.read(new File("NotFlappyBird-main/Images/pixelartbackground.png")); //main menu background
            img1 = ImageIO.read(new File("NotFlappyBird-main/Images/NotFlappyBirdTitle.png")); //title image NotFlappyBird
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Visualizes the loaded images in the entire menu panel. We use paintComponent to visualize the main menu image as well as buttons and the title.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, imgX, 0, null);
        g.drawImage(img, imgX + img.getWidth(), 0, null);
        if (imgX + img.getWidth() < getWidth()) {                       //The background image is repainted if it has moved its entire length
            g.drawImage(img, imgX + img.getWidth() * 2, 0, null);
        }
        g.drawImage(img1, 30, 25, null);
        startButton.draw(g);
        shopButton.draw(g);
        settingsButton.draw(g);
    }
    
}
