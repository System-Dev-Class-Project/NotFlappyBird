package flappymappydeluxe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class BirdTestAnimation {

    private BufferedImage[] frames; // Array to hold bird frames
    private int currentFrameIndex; // Index of the current frame
    private int animationSpeed = 8; // Adjust this to control the speed of animation
    private int frameCounter = 0; // Counter to control animation speed

    private static int bird_dia = 36;
    private static int mushroom_dia = 10;
    private boolean mushroom = false;
    public static int x = (GamePanel.WIDTH / 2) - bird_dia / 2;
    public static int y = GamePanel.HEIGHT / 2; // Spawns the bird in the middle of the window

    private static int speed = 2;
    private int accel = 1;

    public BirdTestAnimation() {
        LoadImages(mushroom);
        currentFrameIndex = 0;
        this.x = x;
        this.y = y;
    }

    private void LoadImages(boolean mushroom) {
        this.mushroom = mushroom;
        if (mushroom) {
            bird_dia = mushroom_dia;
            // Load the smaller bird images
            frames = new BufferedImage[2];
            try {
                frames[0] = ImageIO.read(new File("Images\\redbird-midflap - Kopie.png"));
                frames[1] = ImageIO.read(new File("Images\\redbirdnewflap - Kopie.png"));
            } catch (IOException e) {
                System.out.println("small bird not found");
                e.printStackTrace();
            }
        } else {
            bird_dia = 36;
            // Load the normal bird images
            frames = new BufferedImage[2];
            try {
                frames[0] = ImageIO.read(new File("Images\\redbird-midflap.png"));
                frames[1] = ImageIO.read(new File("Images\\redbirdnewflap.png"));
            } catch (IOException e) {
                System.out.println("bird not found");
                e.printStackTrace();
            }
        }
    }

    public void drawBird(Graphics g) {
        if (InvincibilityPower.isInvincible()) {
            // Set the bird to blink in rainbow colors
            g.drawImage(getColoredImage(frames[currentFrameIndex], getRainbowColor(frameCounter / 2)), x, y, null);
        } else {
            g.drawImage(frames[currentFrameIndex], x, y, null);
        }
    }

    public void birdMovement() {
        if (y >= 0 && y <= GamePanel.HEIGHT) {
            speed += accel;
            y += speed;
        } else {
            GamePanel.sendScoreToServer(GamePanel.score); // Send the score to the server (if the user is logged in)
            boolean option = GamePanel.popUpMessage();

            if (option) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                reset();
            } else {
                JFrame frame = FlappyClass.getWindow();
                frame.dispose();
                FlappyClass.timer.stop();
            }
        }
        animate(); // Call the animate method to update the bird's animation frame
    }

    public void goUpwards() {
        speed = -17;
    }

    public static Rectangle getBirdRect() {
        return new Rectangle(x, y, bird_dia, 35);
    }

    private void animate() {
        frameCounter++;
        if (frameCounter >= animationSpeed) {
            // Toggle between frames
            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
            frameCounter = 0;
        }
    }

    public static void reset() {
        speed = 2;
        y = GamePanel.HEIGHT / 2;
        GamePanel.GameOver = true;
        GamePanel.score = 0;
    }

    public int getX() {
        return x;
    }

    public boolean isMushroom() {
        return mushroom;
    }

    public void setMushroom(boolean b) {
        mushroom = b;
        LoadImages(mushroom);
    }

    public int getDiameter() {
        return bird_dia;
    }

    public int getWidth() {
        return 36;
    }

    public int getHeight() {
        return 36;
    }

    public int getY() {
        return y;
    }

    private Color getRainbowColor(int frame) {
        int r = (int) (Math.sin(0.1 * frame + 0) * 127 + 128);
        int g = (int) (Math.sin(0.1 * frame + 2) * 127 + 128);
        int b = (int) (Math.sin(0.1 * frame + 4) * 127 + 128);
        return new Color(r, g, b);
    }

    private BufferedImage getColoredImage(BufferedImage src, Color color) {
        BufferedImage coloredImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int rgba = src.getRGB(x, y);
                if ((rgba >> 24) != 0x00) { // Only color non-transparent pixels
                    coloredImage.setRGB(x, y, color.getRGB());
                }
            }
        }
        return coloredImage;
    }
}
