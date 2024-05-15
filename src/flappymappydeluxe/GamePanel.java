package flappymappydeluxe;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static boolean GameOver = false;
	
	public static int score = 0;
	
	
	
	public static final int WIDTH =600;
	public static final int HEIGHT =800;
	
	
	private int xCoor=0;
	private BufferedImage img;
	
	BirdTestAnimation bi= new BirdTestAnimation();
	WallImage wi = new WallImage(GamePanel.WIDTH);
	WallImage wi2 = new WallImage (GamePanel.WIDTH+(GamePanel.WIDTH/2));
	CoinImage coinForWi = new CoinImage(wi);
	CoinImage coinForWi2 = new CoinImage(wi2);
	
	public GamePanel() {
		LoadImage();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);  //mousePressed doesnt require release of mouse trigger (mouseClicked needs release)
				bi.goUpwards(); //bird moves up
			}
		});
		
		
		
	}
	
	private void LoadImage() {
		
		
		
		
		try {
	
			img = ImageIO.read(new File ("Images/origbigtruesize.png"));
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
		
		g.setFont(new Font("Tahoma", Font.BOLD, 40));   //displays score
		g.drawString(("Score "+score), 250, 75); //positions the displayed count
		g.drawString("Coins: " + CoinImage.getCoinCount(), 20, 700);
		
		
	
	}
	public void Move() {
		bi.birdMovement();
		wi.wallMovement(coinForWi);
		wi2.wallMovement(coinForWi2);
		coinForWi.moveCoin();
        coinForWi2.moveCoin();
		 
		if (GameOver) {        //if the GameOver variable is true, we reset the wall coordinates and reset GameOver to false               
			wi.X=GamePanel.WIDTH;
			wi2.X=GamePanel.WIDTH+(GamePanel.WIDTH/2);
			coinForWi.setX(wi.X);
			coinForWi2.setX(wi2.X);
			GameOver = false;
		}
		
		xCoor+= WallImage.speed; //we move the background with the same speed as the walls
		
		if (xCoor ==-2400) {
			xCoor=0;
		}
	

	
	
	if (wi.X==BirdImage.x || wi2.X==BirdImage.x) {  //whenever the bird passes the tubes, the score increases by one!
		score+=1;                                  //the code will only execute properly if bird diameter is 36!!! 
		                                 
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
	
	

	
	
	
	
	
