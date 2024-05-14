package NotFlappyBirdPackage;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PowerUp_Invincibility {
	private BufferedImage PowImg;
	private static boolean active = false;
	public int x, y;
    private final int diameter = 25; // Size of the coin
	
	public static void spawnPowerUp() {
        // Logic to spawn the power-up (e.g., set position, activate flag)
        active = true;
    }

    public static boolean isActive() {
        return active;
    }

    public static void deactivatePowerUp() {
        // Logic to deactivate the power-up (e.g., reset position, deactivate flag)
        active = false;
    }
}
