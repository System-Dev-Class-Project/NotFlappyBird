package flappymappydeluxe;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
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
	
	private int xCoor=0;
	private BufferedImage img;
	
	BirdTestAnimation bi= new BirdTestAnimation();
	WallImage wi = new WallImage(GamePanel.WIDTH);
	WallImage wi2 = new WallImage (GamePanel.WIDTH+(GamePanel.WIDTH/2));
	CoinImage coinForWi = new CoinImage(wi, bi);
	CoinImage coinForWi2 = new CoinImage(wi2, bi);
	InvincibilityPower invPower = new InvincibilityPower(wi);
	MushroomPowerUp muPower= new MushroomPowerUp(wi);
	HeartsPowerUp heartPower = new HeartsPowerUp(wi);
	MagnetPowerUp magnetPower = new MagnetPowerUp(bi);
	List<AttractableObject> coins = new ArrayList<>();
	List<AttractableObject> powerUps = new ArrayList<>();
	List<Enemy> enemies = new ArrayList<>();
	Enemy_Gumba enemyGumba = new Enemy_Gumba(bi, wi);
	Enemy_Magic_Firebaaaall enemyFireball = new Enemy_Magic_Firebaaaall(bi, wi);
	Enemy_Alien enemyAlien = new Enemy_Alien(bi, wi);
	Enemy_Batman enemyBatman = new Enemy_Batman(bi, wi);
	DifficultyManagement diff = new DifficultyManagement();	

   
	
	public GamePanel() {
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
	
			img = ImageIO.read(new File ("Images\\origbigtruesize.png"));
		}catch(Exception ex) {
	ex.printStackTrace();
	}
}
	public void paint(Graphics g) {
		
		super.paint(g);
		
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
		bi.birdMovement();
		wi.wallMovement(coinForWi, bi, invPower);
		wi2.wallMovement(coinForWi2, bi, invPower);
		coinForWi.moveCoin();
        coinForWi2.moveCoin();

		for (AttractableObject powerUp : powerUps) {
			powerUp.movePowerUp(wi, bi);
		}
		
		magnetPower.attractObjects(coins);

		for (Enemy enemy : enemies) {
    	enemy.moveEnemy(wi);
		}
		diff.spawnRandomPowerUp(wi, wi2, powerUps);
		diff.spawnRandomEnemies(wi, wi2, enemies);

		 
		if (GameOver) {        //if the GameOver variable is true, we reset the wall coordinates and reset GameOver to false               
			wi.X=GamePanel.WIDTH;
			wi2.X=GamePanel.WIDTH+(GamePanel.WIDTH/2);
			coinForWi.setX(wi.X+10);
			coinForWi2.setX(wi2.X+10);
			GameOver = false;
			wi.hasPassed = false;
			invPower.setVisible=false;
			muPower.setVisible=false;
		}
		
		xCoor+= WallImage.speed; //we move the background with the same speed as the walls
		
		if (xCoor ==-2400) {
			xCoor=0;
		}
	
	
		if (!wi.hasPassed && wi.X <= BirdImage.x) {  //for the 1st column
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
	
	public static boolean popUpMessage() {  //Game Over pop up message with text plus the score
		int result = JOptionPane.showConfirmDialog(null, "Game Over, your score is "+ GamePanel.score+ "\n Do you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);
		if (result== JOptionPane.YES_OPTION) {
			return true; 
		}
		else {
			return false;
		}
	}
}
	
	

	
	
	
	
	
