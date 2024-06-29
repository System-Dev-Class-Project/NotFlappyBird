package flappymappydeluxe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

// MenuPanel class represents the main menu of the game.
public class MenuPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Button startButton;  // Button to start the game
    private Button shopButton;  // Button to enter the shop
    private Button settingsButton;  // Button to enter the settings
    private BufferedImage img;  // Background image
    private BufferedImage img1;  // Coin image
    private BufferedImage img2;  // Title image
    public boolean StartingPoint = false;
    private int imgX = 0; // Initial x-coordinate of the background image
    public static AudioPlayer audioPlayer; // Audio player for background music
    private Timer visibilityCheckTimer; // Timer for background movement

    private CardLayout cardLayout; // Layout for switching panels
    private JPanel mainPanel; // Main panel to switch between different panels

    // Constructor to initialize the menu panel
    public MenuPanel(CardLayout cardLayout, JPanel mainPanel, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        LoadImage(); // Load images for the menu
        audioPlayer.play("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Play background music

        // Timer to update background position and repaint the panel
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imgX -= 2; // Move background to the left

                // Wrap background image to create continuous movement
                if (imgX <= -img.getWidth()) {
                    imgX = 0;
                }

                repaint(); // Repaint the panel to update the background
            }
        });
        timer.start();

        // Initialize buttons with positions, sizes, and images
        startButton = new Button(200, 225, 225, 125, "", "NotFlappyBird-main/Images/PlayButton.png");
        shopButton = new Button(210, 340, 175, 90, "", "NotFlappyBird-main/Images/ShopButton.png");
        settingsButton = new Button(215, 440, 165, 75, "", "NotFlappyBird-main/Images/SettingsButton.png");

        // Mouse listener for button clicks
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (startButton.isClicked(e)) {
                    FlappyClass.restartGame();  // Restart the game
                    StartingPoint = true;
                    cardLayout.show(mainPanel, "game"); // Show game panel
                    switchMusic("NotFlappyBird-main/Music/1-04.-Strike-the-Earth_-_Plains-of-Passage_.wav");
                    System.out.println("Play Button clicked");
                }

                if (shopButton.isClicked(e)) {
                    cardLayout.show(mainPanel, "shop"); // Show shop panel
                    switchMusic("NotFlappyBird-main/Music/Ace-Attorney-18-Marvin-Grossberg-_-Age_-Regret_-Retribution.wav");
                    System.out.println("Shop Button clicked");
                }
                if (settingsButton.isClicked(e)) {
                    cardLayout.show(mainPanel, "settings"); // Show settings panel
                    audioPlayer.stop(); // Stop current music
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

    // Method to load images for the menu panel
    private void LoadImage() {
        try {
            img = ImageIO.read(new File("NotFlappyBird-main/Images/pixelartbackground.png"));
            img1 = ImageIO.read(new File("NotFlappyBird-main/Images/CoinBig.png"));
            img2 = ImageIO.read(new File("NotFlappyBird-main/Images/NotFlappyBirdTitle.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Method to paint the menu panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, imgX, 0, null); // Draw background image
        g.drawImage(img, imgX + img.getWidth(), 0, null); // Draw second background image for seamless transition
        if (imgX + img.getWidth() < getWidth()) {
            g.drawImage(img, imgX + img.getWidth() * 2, 0, null); // Draw third background image if needed
        }
        g.drawImage(img2, 30, 25, null); // Draw title image
        startButton.draw(g); // Draw start button
        shopButton.draw(g); // Draw shop button
        settingsButton.draw(g); // Draw settings button
    }

    // Method to move the menu background
    public void MenuMove() {
        imgX += WallImage.speed;
        if (imgX == -2048) {
            imgX = 0;
        }
    }
}
