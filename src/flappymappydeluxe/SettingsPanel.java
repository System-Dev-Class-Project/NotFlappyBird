package flappymappydeluxe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SettingsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JButton resetHighscoreButton;
    private JButton backButton;

    public SettingsPanel(GamePanel gamePanel, DifficultyManagement difficultyManagement) {
        setLayout(new FlowLayout());

        resetHighscoreButton = new JButton("Reset Highscore");
        resetHighscoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficultyManagement.resetHighscoreOnServer();
                JOptionPane.showMessageDialog(null, "Highscore has been reset.");
            }
        });

        backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FlappyClass.showMenuPanel();
            }
        });

        add(resetHighscoreButton);
        add(backButton);
    }
}
