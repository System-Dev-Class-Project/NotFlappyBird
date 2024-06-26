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

    private AudioPlayer audioPlayer;
    private static int speed = 2;
    private int accel = 1;

    private boolean rainbowColor = false; // Variable for rainbow colors
    private int rainbowOffset = 0; // Offset for rainbow color animation

    public BirdTestAnimation() {
        LoadImages(mushroom);
        currentFrameIndex = 0;
        this.x = x;
        this.y = y;
    }

    private void LoadImages(boolean mushroom) {
    	this.mushroom = mushroom;
        frames = new BufferedImage[2];
        String activeBirdSkin = ShopPanel.getActiveBirdSkinIdle();

        try {
            if (mushroom) {
                bird_dia = mushroom_dia;
                // Load the smaller bird images
                if (activeBirdSkin != null) {
                    switch (activeBirdSkin) {
                        case "Purple":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdFlap_small.png"));
                            break;
                        case "Blue":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdFlap_small.png"));
                            break;
                        case "The Original":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdIdle_small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdFlap_small.png"));
                            break;
                        case "Original":
                        default:
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap-small.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap-small.png"));
                            break;
                    }
                } else {
                    frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap-small.png"));
                    frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap-small.png"));
                }
            } else {
                bird_dia = 36;
                System.out.println("Mushroom is false");
                // Load the normal bird images
                if (activeBirdSkin != null) {
                    switch (activeBirdSkin) {
                        case "Purple":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purpleBirdFlap.png"));
                            break;
                        case "Blue":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBirdFlap.png"));
                            break;
                        case "The Original":
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdIdle.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/yellowBirdFlap.png"));
                            break;
                        case "Original":
                        default:
                            frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap.png"));
                            frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap.png"));
                            break;
                    }
                } else {
                    frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap.png"));
                    frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap.png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawBird(Graphics g) {
        if (InvincibilityPower.isInvincible() && rainbowColor) {
            drawRainbowBird(g); // Draw rainbow bird if invincible and rainbowColor is true
        } else {
            g.drawImage(frames[currentFrameIndex], x, y, null); // Draw normal bird frames
        }
    }

    private void drawRainbowBird(Graphics g) {
        // Draw the bird with rainbow colors
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};

        for (int i = 0; i < frames.length; i++) {
            Color color = colors[(i + rainbowOffset) % colors.length];
            g.setColor(color);
            g.drawImage(frames[i], x, y, null);
        }

        // Update rainbow offset for animation
        rainbowOffset = (rainbowOffset + 1) % colors.length;
    }

    public void setBirdImages(BufferedImage birdImage1, BufferedImage birdImage2) {
        frames[0] = birdImage1;
        frames[1] = birdImage2;
    }

    public void birdMovement(AudioPlayer audioPlayer) {
        if (y >= 0 && y <= GamePanel.HEIGHT) {
            speed += accel;
            y += speed;
        } else {
            audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
            audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
            GamePanel.sendScoreToServer(GamePanel.score);

            int option = GamePanel.popUpMessage();
            if (option == 0) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                reset();
            } else if (option == 2) {
                JFrame frame = FlappyClass.getWindow();
                MenuPanel.audioPlayer.stop();
                frame.dispose();
                FlappyClass.timer.stop();
            } else {
                MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav");
                reset();
                FlappyClass.timer.stop();
                FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
            }
        }
        animate();
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

    public void setRainbowColor(boolean rainbowColor) {
        this.rainbowColor = rainbowColor;
    }
}
