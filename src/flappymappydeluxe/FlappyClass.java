package flappymappydeluxe;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappyClass {

    // Main game window
    private static JFrame window;
    static Timer timer; // Timer to control game loop
    private AudioPlayer audioPlayer; // Audio player for background music
    public static CardLayout cardLayout; // Layout manager for switching between panels
    public static JPanel mainPanel; // Main panel containing different panels
    private ShopPanel shopPanel; // Shop panel
    private static GamePanel gamePanel; // Game panel
    private MenuPanel menuPanel; // Main menu panel
    private SettingsPanel settingsPanel; // Settings panel
    private DifficultyManagement difficultyManagement; // Difficulty management

    // Constructor to initialize the game window and its components
    private FlappyClass() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminate program on close
        window.setSize(GamePanel.WIDTH, GamePanel.HEIGHT); // Set window size
        window.setLocationRelativeTo(null); // Center the window
        window.setTitle("Not Flappy Bird"); // Set window title
        window.setResizable(true); // Allow window resizing
        audioPlayer = new AudioPlayer(true); // Initialize audio player
        difficultyManagement = new DifficultyManagement(); // Initialize difficulty management
    }

    // Method to set up and render the game panels
    private void rendering() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize game panels
        shopPanel = new ShopPanel(cardLayout, mainPanel);
        settingsPanel = new SettingsPanel(cardLayout, mainPanel, difficultyManagement, audioPlayer);
        menuPanel = new MenuPanel(cardLayout, mainPanel, audioPlayer);
        gamePanel = new GamePanel(shopPanel, cardLayout, mainPanel, difficultyManagement);

        // Add panels to the main panel
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");
        mainPanel.add(shopPanel, "shop");
        mainPanel.add(settingsPanel, "settings");

        // Add main panel to the window
        window.add(mainPanel);
        cardLayout.show(mainPanel, "menu"); // Show menu panel first

        window.setVisible(true); // Make window visible

        // Timer to update and repaint the game components
        timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.repaint(); // Repaint game panel
                gamePanel.Move(); // Move game components
                menuPanel.repaint(); // Repaint menu panel
                menuPanel.MenuMove(); // Move menu components
            }
        });
    }

    // Getter for the main game window
    public static JFrame getWindow() {
        return window;
    }

    // Main method to start the game
    public static void main(String[] args) {
        FlappyClass fc = new FlappyClass(); // Create game instance
        fc.rendering(); // Render game panels
    }

    // Method to start the game timer
    public static void startGame() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
    }

    // Method to stop the game timer
    public static void stopGame() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    // Method to restart the game
    public static void restartGame() {
        if (timer != null) {
            stopGame(); // Stop the game timer
            BirdTestAnimation.reset(); // Reset bird animation
            startGame(); // Restart the game timer
        }
    }
}
