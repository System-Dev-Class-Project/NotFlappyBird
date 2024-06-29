package flappymappydeluxe;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
        addSetting(settingsContainer, "Player Name", value -> difficulty.setPlayerName(String.valueOf(value)), () -> String.valueOf(difficulty.getPlayerName()));
        addSetting(settingsContainer, "Score rate at which enemies spawn", value -> difficulty.setEnemyScore(Integer.parseInt(value)), () -> String.valueOf(difficulty.getEnemyScore()));
        addSetting(settingsContainer, "Score rate at which powerups spawn", value -> difficulty.setPowerupScore(Integer.parseInt(value)), () -> String.valueOf(difficulty.getPowerupScore()));
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

        // Center the settings container
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(settingsContainer);
        add(centerPanel, BorderLayout.CENTER);

        // Create and add reset highscore button
        JButton resetHighscoreButton = createButton("Reset Highscore");
        resetHighscoreButton.setPreferredSize(new Dimension(150, 40)); // Smaller size for the reset button
        resetHighscoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DifficultyManagement.resetHighscoreOnServer();
                JOptionPane.showMessageDialog(null, "Highscore has been reset!");
            }
        });

        JPanel resetButtonPanel = new JPanel(new GridBagLayout());
        resetButtonPanel.setOpaque(false);
        resetButtonPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Increase space between reset and back button
        resetButtonPanel.add(resetHighscoreButton);
        add(resetButtonPanel, BorderLayout.SOUTH); // Add reset button to the south panel

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

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.add(backButton);

        // Use a nested panel structure to place the reset button above the back button
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(resetButtonPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH); // Add the nested panel to the bottom of the main panel

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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false); // Make the panel transparent
        panel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Add more space between settings
        JLabel label = new JLabel(labelName);
        label.setForeground(Color.BLACK); // Set label text color to black
        JTextField textField = new JTextField(10);
        JButton button = new JButton("Set " + labelName);
        button.setForeground(Color.BLACK); // Set button text color to black

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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 240, 240), 0, getHeight(), new Color(200, 200, 200));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK); // Set button text color to black
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(new LineBorder(Color.BLACK, 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(new LineBorder(Color.RED, 2));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(new LineBorder(Color.BLACK, 2));
            }
        });

        return button;
    }
}
