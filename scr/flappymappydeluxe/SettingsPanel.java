package flappymappydeluxe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsPanel extends JPanel {
    private JButton backButton;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel settingsContainer; // Panel für die Einstellungs-Buttons
    private DifficultyManagement difficulty;

    public SettingsPanel(CardLayout cardLayout, JPanel mainPanel, DifficultyManagement difficulty) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        // Erstellen des Zurück-Buttons
        backButton = createButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hier die Logik zum Wechseln der Musik und Ansicht
                cardLayout.show(mainPanel, "menu");
                System.out.println("Back to Menu");
            }
        });

        JButton resetHighscoreButton = createButton("Reset Highscore");
        resetHighscoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DifficultyManagement.resetHighscoreOnServer();
                JOptionPane.showMessageDialog(null, "Highscore has been reset!");
            }
        });

        // Panel für die Einstellungs-Buttons initialisieren
        settingsContainer = new JPanel();
        settingsContainer.setLayout(new BoxLayout(settingsContainer, BoxLayout.Y_AXIS));

        // Hinzufügen der Einstellungs-Buttons zum settingsContainer
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

        // Hinzufügen des settingsContainer zum Hauptpanel
        add(settingsContainer, BorderLayout.CENTER);
        settingsContainer.add(resetHighscoreButton);

        // Hinzufügen des Zurück-Buttons zum unteren Bereich des Hauptpanels
        add(backButton, BorderLayout.SOUTH);
    }

    private void addSetting(JPanel container, String labelName, Consumer<String> setter, Supplier<String> getter) {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel(labelName);
    JTextField textField = new JTextField(10);
    JButton button = new JButton("Set " + labelName);

    // Update the button action to refresh the label with the current value from the getter
    button.addActionListener(e -> {
        setter.accept(textField.getText());
        label.setText(labelName + ": " + getter.get()); // Update the label with the new value
        textField.setText(""); // Clear the field after setting
    });

    // Initialize the label with the current value from the getter
    label.setText(labelName + ": " + getter.get());

    panel.add(label);
    panel.add(textField);
    panel.add(button);
    container.add(panel); // Add the panel to the settingsContainer
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
