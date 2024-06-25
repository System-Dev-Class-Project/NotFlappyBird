package flappymappydeluxe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DifficultyManagement difficulty;
    private AudioPlayer audioPlayer;
    private BufferedImage backgroundImage;

    public SettingsPanel(CardLayout cardLayout, JPanel mainPanel, DifficultyManagement difficulty, AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.difficulty = difficulty;
        setLayout(new BorderLayout());

        // Load the background image
        loadBackgroundImage();

        // Create settings container
        JPanel settingsContainer = new JPanel();
        settingsContainer.setLayout(new BoxLayout(settingsContainer, BoxLayout.Y_AXIS));
        settingsContainer.setOpaque(false); // Make settings container transparent

        // Add settings to the settings container
        addSetting(settingsContainer, "Enemy Score", value -> difficulty.setEnemyScore(Integer.parseInt(value)), () -> String.valueOf(difficulty.getEnemyScore()));
        addSetting(settingsContainer, "Powerup Score", value -> difficulty.setPowerupScore(Integer.parseInt(value)), () -> String.valueOf(difficulty.getPowerupScore()));
        addSetting(settingsContainer, "Speed", value -> difficulty.setSpeed(Integer.parseInt(value)), () -> String.valueOf(difficulty.getSpeed()));
        addSetting(settingsContainer, "Hearts", value -> difficulty.setHearts(Integer.parseInt(value)), () -> String.valueOf(difficulty.getHearts()));
        addSetting(settingsContainer, "Multiple Enemies", value -> difficulty.setMultipleEnemies(Integer.parseInt(value)), () -> String.valueOf(difficulty.getMultipleEnemies()));
        addSetting(settingsContainer, "PowerUp Probabilities", value -> {
            String[] values = value.split(",");
            double[] probabilities = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                probabilities[i] = Double.parseDouble(values[i].trim());
            }
            difficulty.setPowerUpProbabilities(probabilities);
        }, () -> Arrays.toString(difficulty.getPowerUpProbabilities()));

        // Add reset highscore button
        JButton resetHighscoreButton = createButton("Reset Highscore");
        resetHighscoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DifficultyManagement.resetHighscoreOnServer();
                JOptionPane.showMessageDialog(null, "Highscore has been reset!");
            }
        });
        settingsContainer.add(resetHighscoreButton);

        // Add settings container to main panel
        add(settingsContainer, BorderLayout.CENTER);

        // Create and add back button
        JButton backButton = createButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioPlayer.play("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                cardLayout.show(mainPanel, "menu");
                System.out.println("Back to Menu");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom of the main panel

        // Debug prints
        System.out.println("SettingsPanel initialized");
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("NotFlappyBird-main/Images/SettingsBackground.png"));
            System.out.println("Background image loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load background image");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void addSetting(JPanel container, String labelName, Consumer<String> setter, Supplier<String> getter) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false); // Make the panel transparent
        JLabel label = new JLabel(labelName);
        JTextField textField = new JTextField(10);
        JButton button = new JButton("Set " + labelName);

        button.addActionListener(e -> {
            setter.accept(textField.getText());
            label.setText(labelName + ": " + getter.get());
            textField.setText("");
        });

        label.setText(labelName + ": " + getter.get());

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        container.add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(240, 228, 204));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }
}
