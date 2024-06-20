package flappymappydeluxe;

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
    private Map<String, AttractableObject> powerUpMap = new HashMap<>();
    private Map<String, Enemy> enemyMap = new HashMap<>();

    public DifficultyManagement(BirdTestAnimation player, WallImage wall, WallImage wall2) {
        initializePowerUps(player, wall);
        initializeEnemies(player, wall);
        powerUpProbabilities = new double[]{0.3, 0.5, 0.2}; // Adjust as needed
        spawnEnemiesBasedOnScore(wall, wall2);
        spawnRandomPowerUp(wall, wall2);
    }

    private void initializePowerUps(BirdTestAnimation player, WallImage wall) {
        // Example initialization, adjust according to your game
        powerUpMap.put("Magnet", new MagnetPowerUp(player));
        powerUpMap.put("Invincibility", new InvincibilityPower(wall));
        powerUpMap.put("Mushroom", new MushroomPowerUp(wall));
        powerUpMap.put("Hearts", new HeartsPowerUp(wall));

        powerUps.addAll(powerUpMap.values());
    }

    private void initializeEnemies(BirdTestAnimation player, WallImage wall) {
        // Example initialization, adjust according to your game
        enemyMap.put("Alien", new Enemy_Alien(player, wall));
        enemyMap.put("Gumba", new Enemy_Gumba(player, wall));
        enemyMap.put("Batman", new Enemy_Batman(player, wall));
        enemyMap.put("Fireball", new Enemy_Magic_Firebaaaall(player, wall));

        enemies.addAll(enemyMap.values());
    }

    private void spawnRandomPowerUp(WallImage wall, WallImage wall2) {
        double randomValue = random.nextDouble(); // Generate a random value between 0.0 and 1.0
        double cumulativeProbability = 0.0;
        int selectedPowerUpIndex = -1;
        for (int i = 0; i < powerUpProbabilities.length; i++) {
            cumulativeProbability += powerUpProbabilities[i];
            if (randomValue <= cumulativeProbability) {
                selectedPowerUpIndex = i;
                break;
            }
        }
        // Proceed only if a valid power-up was selected
        if (selectedPowerUpIndex != -1) {
            AttractableObject selectedPowerUp = powerUps.get(selectedPowerUpIndex);
            WallImage targetWall = GamePanel.score % 2 == 0 ? wall : wall2; // Choose wall based on score
            selectedPowerUp.spawn(targetWall); // Adjust position to the target wall
        }
           
        }

    private void spawnEnemiesBasedOnScore(WallImage wall, WallImage wall2) {
        int enemiesToSpawn = 1; // Default to 1 enemy
        if (GamePanel.score >= 10) {
            double chance = Math.min(1.0, (GamePanel.score - 10) / 100.0); // Example scaling
            if (random.nextDouble() < chance) {
                enemiesToSpawn += random.nextInt(3); // 1 to 3 enemies
            }
        }

        for (int i = 0; i < enemiesToSpawn; i++) {
            Enemy enemy = enemies.get(random.nextInt(enemies.size()));
            WallImage targetWall = GamePanel.score % 2 == 0 ? wall : wall2; // Choose wall based on score
            enemy.spawn(targetWall); // Adjust position to the target wall
        }
    }


}