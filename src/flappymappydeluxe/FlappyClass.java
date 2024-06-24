package flappymappydeluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyClass {
    private static JFrame window;
    public static Timer timer;
    private static MenuPanel mp;
    private static GamePanel gp;
    private static SettingsPanel sp;
    private static DifficultyManagement difficultyManagement;

    private FlappyClass() {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminates the program after window is closed
        window.setSize(GamePanel.WIDTH, GamePanel.HEIGHT); // width, height
        window.setLocationRelativeTo(null); // sets window pop-up location to screen center
        window.setTitle("Not Flappy Bird"); // title
        window.setResizable(true); // prohibits window resizing
    }

    private void rendering() {
        difficultyManagement = new DifficultyManagement();
        mp = new MenuPanel();
        gp = new GamePanel();
        sp = new SettingsPanel(gp, difficultyManagement);

        timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gp.repaint(); // its calling the paint method iteratively
                gp.Move();
                mp.repaint();
                mp.MenuMove();

                if (mp.OpenSettings) {
                    showSettingsPanel();
                }

                if (GamePanel.GameOver) {
                    gp.sendScoreToServer(GamePanel.score);
                    boolean restart = GamePanel.popUpMessage();
                    if (restart) {
                        GamePanel.score = 0; // Reset score
                    } else {
                        timer.stop();
                        window.dispose(); // Close the game window
                        System.exit(0); // Terminate the program
                    }
                }
            }
        });

        window.add(mp);
        window.setVisible(true);
        while (!mp.StartingPoint && !mp.OpenSettings) {
            try {
                Thread.sleep(5);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mp.StartingPoint) {
            window.remove(mp);
            window.add(gp);
            gp.setVisible(true);
            window.revalidate();
            timer.start();
        }
    }

    public static void showSettingsPanel() {
        window.remove(mp);
        window.add(sp);
        sp.setVisible(true);
        window.revalidate();
    }

    public static void showMenuPanel() {
        window.remove(sp);
        window.add(mp);
        mp.setVisible(true);
        window.revalidate();
    }

    public static JFrame getWindow() {
        return window;
    }

    public static void main(String[] args) {
        FlappyClass fc = new FlappyClass();
        fc.rendering();
    }
}
