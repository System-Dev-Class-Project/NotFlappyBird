package flappymappydeluxe;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappyClass {

    private static JFrame window;
    static Timer timer;
    private AudioPlayer audioPlayer;
    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    private ShopPanel shopPanel;
    private static GamePanel gamePanel;
    private MenuPanel menuPanel;

    public FlappyClass() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminates the program after window is closed
        window.setSize(GamePanel.WIDTH, GamePanel.HEIGHT); // width, height
        window.setLocationRelativeTo(null); // sets window pop-up location to screen center
        window.setTitle("Not Flappy Bird"); // title
        window.setResizable(false);  // prohibits window resizing
        audioPlayer = new AudioPlayer();
    }

    private void rendering() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize ShopPanel first
        shopPanel = new ShopPanel(cardLayout, mainPanel);

        // Now initialize MenuPanel and GamePanel with the initialized shopPanel
        menuPanel = new MenuPanel(cardLayout, mainPanel, audioPlayer);
        gamePanel = new GamePanel(shopPanel, cardLayout, mainPanel);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");
        mainPanel.add(shopPanel, "shop");

        window.add(mainPanel);
        cardLayout.show(mainPanel, "menu"); // Show menu panel first

        window.setVisible(true);

        timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gamePanel.repaint(); // it's calling the paint method iteratively
                gamePanel.Move();
                menuPanel.repaint();
                menuPanel.MenuMove();
            }
        });

       
    }

    public static JFrame getWindow() {
        return window;
    }

    public static void main(String[] args) {
        FlappyClass fc = new FlappyClass();
        fc.rendering();
    }

    public static void startGame() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
    }

    public static void stopGame() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    // Ensure to restart the game when returning from the main menu
    public static void restartGame() {
        if (timer != null) {
            stopGame();
            BirdTestAnimation.reset();
            
            startGame();
        }
    }
}
