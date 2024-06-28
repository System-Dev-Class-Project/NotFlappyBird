package flappymappydeluxe;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	/**The gamepanel class visualizes the game window itself, here we bring together the bird class, wall class, coin class and other
	 * to be implemented classes that have any in game utility. 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static boolean GameOver = false;
	public static int score = 0;
	public static boolean hasPassed = false;
	public static final int WIDTH =600;
	public static final int HEIGHT =800;
	
	int coinCount = CoinImage.coinCount;
	public static int highScore = 0;
	
	private int xCoor=0;
	private BufferedImage img;
    private BufferedImage pipeSkinImage;
    private BufferedImage birdSkinImage;
    private BufferedImage birdSkinImageFlap;
    private BufferedImage backgroundSkinImage;
	private DifficultyManagement diff;
	private static String name="Player";
	private static String highScoreName="Player";
    AudioPlayer audioPlayer = new AudioPlayer(false);  //for the short sound bytes we define the AudioPlayer with false so that it doesnt repeat itself after occuring once, unlike the background music
	BirdTestAnimation bi= new BirdTestAnimation();
	WallImage wi = new WallImage(GamePanel.WIDTH);
	WallImage wi2 = new WallImage (GamePanel.WIDTH+(GamePanel.WIDTH/2));
	CoinImage coinForWi = new CoinImage(wi, bi, audioPlayer);
	CoinImage coinForWi2 = new CoinImage(wi2, bi, audioPlayer);
	InvincibilityPower invPower = new InvincibilityPower(wi, audioPlayer);
	MushroomPowerUp muPower= new MushroomPowerUp(wi, audioPlayer);
	HeartsPowerUp heartPower = new HeartsPowerUp(wi, audioPlayer);
	MagnetPowerUp magnetPower = new MagnetPowerUp(bi, audioPlayer);
	List<AttractableObject> coins = new ArrayList<>();
	List<AttractableObject> powerUps = new ArrayList<>();
	List<Enemy> enemies = new ArrayList<>();
	Enemy_Gumba enemyGumba = new Enemy_Gumba(bi, wi, audioPlayer);
	Enemy_Magic_Firebaaaall enemyFireball = new Enemy_Magic_Firebaaaall(bi, wi, audioPlayer);
	Enemy_Alien enemyAlien = new Enemy_Alien(bi, wi, audioPlayer);
	Enemy_Batman enemyBatman = new Enemy_Batman(bi, wi,audioPlayer);
	
	

	private ShopPanel shopPanel;
	
	public CardLayout cardLayout;
    public JPanel mainPanel;
    
    
	
	public GamePanel(ShopPanel shopPanel, CardLayout cardLayout, JPanel mainPanel, DifficultyManagement diff) {
		this.shopPanel = shopPanel;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
		this.diff=diff;
		this.highScore = highScore;
        
		LoadImage();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);  //mousePressed doesnt require release of mouse trigger (mouseClicked needs release)
				bi.goUpwards(); //bird moves up
			}
		});
		coins.add(coinForWi);
		coins.add(coinForWi2);
		powerUps.add(invPower);
		powerUps.add(muPower);
		powerUps.add(heartPower);
		powerUps.add(magnetPower);
		enemies.add(enemyGumba);
		enemies.add(enemyFireball);
		enemies.add(enemyAlien);
		enemies.add(enemyBatman);
		coins.addAll(powerUps);

		
	}
	
	private void LoadImage() {
		try {
		img = ImageIO.read(new File("NotFlappyBird-main/Images/origbigtruesize.png"));

        // Load active skins
        String activePipeSkin = shopPanel.getActivePipeSkin();
        String activeBackgroundSkin = shopPanel.getActiveBackgroundSkin();
        String activeBirdSkin = shopPanel.getActiveBirdSkinIdle();

        // Load corresponding images based on active skins
        if (activePipeSkin != null) {
            switch (activePipeSkin) {
                case "Posh Purple":
                    pipeSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/purplePipe.png"));
                    break;
                case "Business Black":
                    pipeSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blackPipe.png"));
                    break;
                case "Royal Blue":
                    pipeSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/bluePipe.png"));
                    break;
                case "Original":
                default:
                    pipeSkinImage = ImageIO.read(new File("NotFlappyBird-main/Images/pipe-greendoublefinal.png"));
                    break;
            }
        } else {
            pipeSkinImage = ImageIO.read(new File("NotFlappyBird-main/Images/pipe-greendoublefinal.png"));
        }

        if (activeBackgroundSkin != null) {
            switch (activeBackgroundSkin) {
                case "Cloudy Blues":
                    backgroundSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueCloudsBackground.png"));
                    break;
                case "Ocean Landscape":
                    backgroundSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/OceanBackground.png"));
                    break;
                case "Metropolis":
                    backgroundSkinImage = ImageIO.read(new File("NotFlappyBird-main/ShopSkins/blueBackgroundCity.png"));
                    break;
                case "Original":
                default:
                    backgroundSkinImage = ImageIO.read(new File("NotFlappyBird-main/Images/origbigtruesize.png"));
                    break;
            }
        } else {
            backgroundSkinImage = ImageIO.read(new File("NotFlappyBird-main/Images/origbigtruesize.png"));
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
	public void reloadImages() {
        LoadImage();
        repaint();
    }

	public void paintComponent(Graphics g) {
		
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
        
		g.drawImage(img,  xCoor,  0,  null);      //in-game background
		g.drawImage(img,  xCoor+2400,  0,  null); //this fixes the background going blank after having moved the entire width of the screen
		bi.drawBird(g);   //the bird
		wi.drawWall(g);  //we display two separate walls iteratively, wi and wi2
		wi2.drawWall(g);
		coinForWi.drawCoin(g);
        coinForWi2.drawCoin(g);

		for (AttractableObject powerUp : powerUps) {
			powerUp.drawPowerUp(g);
		}

		for (Enemy enemy : enemies) {
			enemy.drawPowerUp(g);
		}

		
		g.setFont(new Font("Tahoma", Font.BOLD, 40));   //displays score
		g.drawString(("Score "+score), 250, 75); //positions the displayed count
		g.drawString("Coins: " + CoinImage.getCoinCount()+"heart"+HeartsPowerUp.getHearts(), 20, 700);
		g.drawString("Total Coins: " + CoinImage.loadCoinCount(), 20, 600);
		
	
	}
	public void Move() {
		bi.birdMovement(audioPlayer);
		wi.wallMovement(coinForWi, bi, invPower, audioPlayer);
		wi2.wallMovement(coinForWi2, bi, invPower, audioPlayer);
		coinForWi.moveCoin();
        coinForWi2.moveCoin();

		for (AttractableObject powerUp : powerUps) {
			powerUp.movePowerUp(wi, bi);
		}
		
		magnetPower.attractObjects(coins);

		for (Enemy enemy : enemies) {
    	enemy.moveEnemy(wi);
		}
		diff.spawnRandomPowerUp(wi, wi2, coinForWi, coinForWi2, powerUps);
		diff.spawnRandomEnemies(wi, wi2, enemies);

		 
		if (GameOver) {        //if the GameOver variable is true, we reset the wall coordinates and reset GameOver to false               
			wi.X=GamePanel.WIDTH;
			wi2.X=GamePanel.WIDTH+(GamePanel.WIDTH/2);
			coinForWi.setX(wi.X+10);
			coinForWi.setVisible(true);
			coinForWi2.setX(wi2.X+10);
			coinForWi2.setVisible(true);
			GameOver = false;
			wi.hasPassed = false;
			for (Enemy enemy : enemies) {
				enemy.setVisible(false);
			}
			for (AttractableObject powerUp : powerUps) {
				powerUp.setVisible(false);;
			}
		}
		
		xCoor+= WallImage.speed; //we move the background with the same speed as the walls
		
		if (xCoor ==-2400) {
			xCoor=0;
		}
	
	
		if (!wi.hasPassed && wi.X <= BirdTestAnimation.x) {  //for the 1st column
	        score++;
	        wi.hasPassed = false;
	    }



	    // Gradually increase the speed based on the score
	    WallImage.speed -= (score / 100);

	    // Ensure that the speed does not become too fast or too slow
	    if (WallImage.speed < -10) {
	        WallImage.speed = -10; // Set a maximum speed limit
	    } else if (WallImage.speed > -2) {
	        WallImage.speed = -2; // Set a minimum speed limit
	    }
	}

	public static void sendScoreToServer(int score) {
		try (Socket socket = new Socket("localhost", 12345);
			 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
	
			// Send the name and score to the server
			String playerName = GamePanel.name;
			out.println(playerName + "," + score);
	
			// Receive the response from the server (high score and name)
			String response = in.readLine();
			String[] responseParts = response.split(",");
			if (responseParts.length == 2) {
				String highScoreName = responseParts[0];
				GamePanel.highScore = Integer.parseInt(responseParts[1]);
				System.out.println("Highscore: " + highScore + " by " + highScoreName);  // Debug print
	
				// Update the game UI or internal state with the high score and name
				// For example:
				// GamePanel.setHighScore(highScore, highScoreName);
			} else {
				System.out.println("Invalid response format: " + response);  // Debug print
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static int popUpMessage() {  //Game Over pop up message with text plus the score
		String[] options = {"Restart", "Back to Main Menu", "Exit"};
        int dialogResult = JOptionPane.showOptionDialog(null, "Game Over, your score is " + GamePanel.score + "\n The current Highscore of " + GamePanel.highScore+" is hold by "+GamePanel.highScoreName, "GAME OVER!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return dialogResult;
	}

	public static void setPlayerName(String name) {
		GamePanel.highScoreName = name;
	}
	public static String getPlayerName() {
		return highScoreName;
	}
}
	
	

	
	
	
	
	
