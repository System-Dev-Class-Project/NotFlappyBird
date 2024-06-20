package flappymappydeluxe;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public static boolean GameOver = false;

    public static int score = 0;
    public static boolean hasPassed = true;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;

    int coinCount = CoinImage.coinCount;

    private int xCoor = 0;
    private BufferedImage img;
    private BufferedImage pipeSkinImage;
    private BufferedImage birdSkinImage;
    private BufferedImage birdSkinImageFlap;
    private BufferedImage backgroundSkinImage;

    BirdTestAnimation bi = new BirdTestAnimation();
    WallImage wi = new WallImage(GamePanel.WIDTH);
    WallImage wi2 = new WallImage(GamePanel.WIDTH + (GamePanel.WIDTH / 2));
    CoinImage coinForWi = new CoinImage(wi);
    CoinImage coinForWi2 = new CoinImage(wi2);

    private ShopPanel shopPanel;

    public static CardLayout cardLayout;
    public static JPanel mainPanel;

    public GamePanel(ShopPanel shopPanel, CardLayout cardLayout, JPanel mainPanel) {
        this.shopPanel = shopPanel;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        LoadImage();
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                bi.goUpwards();
            }
        });
    }

    private void LoadImage() {
        try {
            img = ImageIO.read(new File("Images/origbigtruesize.png"));

            // Load active skins
            String activePipeSkin = shopPanel.getActivePipeSkin();
            String activeBackgroundSkin = shopPanel.getActiveBackgroundSkin();
            String activeBirdSkin = shopPanel.getActiveBirdSkinIdle();

            // Load corresponding images based on active skins
            if (activePipeSkin != null) {
                switch (activePipeSkin) {
                    case "Posh Purple":
                        pipeSkinImage = ImageIO.read(new File("ShopSkins/purplePipe.png"));
                        break;
                    case "Business Black":
                        pipeSkinImage = ImageIO.read(new File("ShopSkins/blackPipe.png"));
                        break;
                    case "Royal Blue":
                        pipeSkinImage = ImageIO.read(new File("ShopSkins/bluePipe.png"));
                        break;
                    case "Original":
                    default:
                        pipeSkinImage = ImageIO.read(new File("Images/pipe-greendoublefinal.png"));
                        break;
                }
            } else {
                pipeSkinImage = ImageIO.read(new File("Images/pipe-greendoublefinal.png"));
            }

            if (activeBackgroundSkin != null) {
                switch (activeBackgroundSkin) {
                    case "Cloudy Blues":
                        backgroundSkinImage = ImageIO.read(new File("ShopSkins/blueCloudsBackground.png"));
                        break;
                    case "Ocean Landscape":
                        backgroundSkinImage = ImageIO.read(new File("ShopSkins/OceanBackground.png"));
                        break;
                    case "Metropolis":
                        backgroundSkinImage = ImageIO.read(new File("ShopSkins/blueBackgroundCity.png"));
                        break;
                    case "Original":
                    default:
                        backgroundSkinImage = ImageIO.read(new File("Images/origbigtruesize.png"));
                        break;
                }
            } else {
                backgroundSkinImage = ImageIO.read(new File("Images/origbigtruesize.png"));
            }

            if (activeBirdSkin != null) {
                switch (activeBirdSkin) {
                    case "Purple":
                        birdSkinImage = ImageIO.read(new File("ShopSkins/purpleBirdIdle.png"));
                        birdSkinImageFlap = ImageIO.read(new File("ShopSkins/purpleBirdFlap.png"));
                        break;
                    case "Blue":
                        birdSkinImage = ImageIO.read(new File("ShopSkins/blueBirdIdle.png"));
                        birdSkinImageFlap = ImageIO.read(new File("ShopSkins/blueBirdFlap.png"));
                        break;
                    case "The Original":
                        birdSkinImage = ImageIO.read(new File("ShopSkins/yellowBirdIdle.png"));
                        birdSkinImageFlap = ImageIO.read(new File("ShopSkins/yellowBirdFlap.png"));
                        break;
                    case "Original":
                    default:
                        birdSkinImage = ImageIO.read(new File("Images/redbird-midflap.png"));
                        birdSkinImageFlap = ImageIO.read(new File("Images/redbirdnewflap.png"));
                        break;
                }
            } else {
                birdSkinImage = ImageIO.read(new File("Images/redbird-midflap.png"));
                birdSkinImageFlap = ImageIO.read(new File("Images/redbirdnewflap.png"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reloadImages() {
        LoadImage();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundSkinImage != null) {
            g.drawImage(backgroundSkinImage, xCoor, 0, null);
            g.drawImage(backgroundSkinImage, xCoor + 2400, 0, null);
        } else {
            g.drawImage(img, xCoor, 0, null);
            g.drawImage(img, xCoor + 2400, 0, null);
        }

        if (birdSkinImage != null && birdSkinImageFlap != null) {
            bi.setBirdImages(birdSkinImage, birdSkinImageFlap);
        }

        if (pipeSkinImage != null) {
            wi.setPipeImage(pipeSkinImage);
            wi2.setPipeImage(pipeSkinImage);
        }

        bi.drawBird(g);
        wi.drawWall(g);
        wi2.drawWall(g);
        coinForWi.drawCoin(g);
        coinForWi2.drawCoin(g);

        g.setFont(new Font("Tahoma", Font.BOLD, 40));
        g.drawString(("Score " + score), 250, 75);
        g.drawString("Coins: " + CoinImage.getCoinCount(), 20, 700);
        g.drawString("Total Coins: " + CoinImage.loadCoinCount(), 20, 600);
    }

    public void Move() {
        bi.birdMovement();
        wi.wallMovement(coinForWi, bi);
        wi2.wallMovement(coinForWi2, bi);
        coinForWi.moveCoin();
        coinForWi2.moveCoin();

        if (GameOver) {
            wi.X = GamePanel.WIDTH;
            wi2.X = GamePanel.WIDTH + (GamePanel.WIDTH / 2);
            coinForWi.setX(wi.X + 10);
            coinForWi2.setX(wi2.X + 10);
            GameOver = false;
            hasPassed = true;
        }

        xCoor += WallImage.speed;

        if (xCoor == -2400) {
            xCoor = 0;
        }

        if (!wi.hasPassed && wi.X <= BirdTestAnimation.x) {
            score++;
            wi.hasPassed = false;
        }

        WallImage.speed -= (score / 100);

        if (WallImage.speed < -10) {
            WallImage.speed = -10;
        } else if (WallImage.speed > -2) {
            WallImage.speed = -2;
        }
    }

    public static int popUpMessage() {
        String[] options = {"Restart", "Back to Main Menu", "Exit"};
        int dialogResult = JOptionPane.showOptionDialog(null, "You lost! What would you like to do?", "GAME OVER!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return dialogResult;
    }
}
