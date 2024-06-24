package flappymappydeluxe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DifficultyManagement {
    private Random random = new Random();
    private List<AttractableObject> powerUps = new ArrayList<>();
    private double[] powerUpProbabilities; // Probabilities for each power-up
    private List<Enemy> enemies = new ArrayList<>();
    private int enemyScore = 4; // Score interval for enemy spawn
    private int powerupScore = 2; // Score interval for power-up spawn
    boolean enemySpawnedForCurrentScore = true;
    boolean powerupSpawnedForCurrentScore = true;

    DifficultyManagement() {
        powerUpProbabilities = new double[]{0.2, 0.3, 0.2, 0.3}; //set probability for each power-up (1=inv, 2, mushroom, 3=heart, 4=magnet)
    }


    void spawnRandomPowerUp(WallImage wall, WallImage wall2, List<AttractableObject> powerupTypes ) {
        if (GamePanel.score % powerupScore == 0 && powerupSpawnedForCurrentScore && GamePanel.score != 0) {
            // Generate a random number between 0 and 1
            double probability = random.nextDouble();
            double cumulativeProbability = 0.0;
            int selectedPowerUpIndex = 0;
    
            // Determine which power-up to spawn based on the probabilities
            for (int i = 0; i < powerUpProbabilities.length; i++) {
                cumulativeProbability += powerUpProbabilities[i];
                if (probability <= cumulativeProbability) {
                    selectedPowerUpIndex = i;
                    break;
                }
            }
    
            // Ensure the selected index is within the bounds of available power-ups
            selectedPowerUpIndex = Math.min(selectedPowerUpIndex, powerupTypes.size() - 1);
    
            // Spawn the selected power-up
            AttractableObject powerup = powerupTypes.get(selectedPowerUpIndex);
            WallImage targetWall = GamePanel.score % 2 == 0 ? wall : wall2;
            System.out.println("Powerup spawned at score " + GamePanel.score);
            powerup.spawn(targetWall);
            powerupSpawnedForCurrentScore = false;
        } else if (GamePanel.score % powerupScore != 0) {
            powerupSpawnedForCurrentScore = true;
        }
    }
            

        void spawnRandomEnemies(WallImage wall, WallImage wall2, List<Enemy> enemyTypes) {
            if (GamePanel.score % enemyScore == 0 && enemySpawnedForCurrentScore && GamePanel.score!=0) { // Nach jedem 3. Score
                int enemiesToSpawn = GamePanel.score > 10 ? random.nextInt(2)+1  : 1; // Spawn 2 oder 3 Feinde, wenn Score > 10, sonst 1
                for (int i = 0; i < enemiesToSpawn; i++) {
                    Enemy enemy = enemyTypes.get(random.nextInt(enemyTypes.size())); // Zufälligen Enemy auswählen
                    WallImage targetWall = GamePanel.score % 2 == 0 ? wall : wall2; // Wall basierend auf Score auswählen
                    System.out.println("Enemy spawned at score " +GamePanel.score);
                    enemy.spawn(targetWall); // Enemy am gewählten Wall spawnen
                    enemySpawnedForCurrentScore = false;
                }
            }else if (GamePanel.score % enemyScore != 0) {
            enemySpawnedForCurrentScore = true; // Zurücksetzen, wenn der Score kein Vielfaches von 3 ist
        }

    }

    public static void resetHighscoreOnServer() {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("RESET_HIGHSCORE");
            String response = in.readLine();
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
