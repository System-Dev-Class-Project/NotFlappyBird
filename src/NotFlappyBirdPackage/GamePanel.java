package NotFlappyBirdPackage;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	
	public static final long serialVersionUID= 1L;
	
	public static boolean GameOver = false;
	public static int score= 0;
	public static boolean starting= false;
	public static int proceed= -1;
	
	
	public static final int WIDTH=600;
	public static final int HEIGHT=800;
	
	private int xCoor=0;
	private BufferedImage img;
	
	BirdImage bi= new BirdImage();
	
	WallImage wi = new WallImage(GamePanel.WIDTH);
	WallImage wi2 =new WallImage(GamePanel.WIDTH+(GamePanel.WIDTH/2));
	CoinImage coinForWi = new CoinImage(wi);
	CoinImage coinForWi2 = new CoinImage(wi2);
	
	
	public GamePanel() {
		LoadImage();
		//mouse prs event
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				bi.goUpwards();
			}
		});
	}

	private void LoadImage() {
		
		try {
			img = ImageIO.read(new File("C:\\Users\\Kevin\\eclipse-workspace\\Not_Flappy_Bird\\Images\\Background.png"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
		
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, xCoor, 0, null);
		g.drawImage(img, xCoor+2400, 0, null); //no unloaded background
		
		
		bi.drawBird(g);
		wi.drawWall(g);
		wi2.drawWall(g);
		coinForWi.drawCoin(g);
        coinForWi2.drawCoin(g);
		
		g.setFont(new Font("Tahoma", Font.BOLD, 40));
		g.drawString("Score "+score, 200, 100);
		g.drawString("Coins: " + CoinImage.getCoinCount(), 20, 700);
		
		if(starting) {
			g.setFont(new Font("Tahoma", Font.BOLD, 150));
			g.drawString(Integer.toString(proceed), WIDTH/2-75, 250);
		}
	}
	
	public void Move() {
		bi.birdMovement();
		wi.wallMovement(coinForWi);
		wi2.wallMovement(coinForWi2);
		coinForWi.moveCoin();
        coinForWi2.moveCoin();
		
		if(GameOver) {
			wi.X=GamePanel.WIDTH;
			wi2.X= GamePanel.WIDTH+(GamePanel.WIDTH/2);
			coinForWi.setX(wi.X);
			coinForWi2.setX(wi2.X);
			GameOver=false;
		}
		
		xCoor+=WallImage.speed;
		if (xCoor==-2400) {
			xCoor=0;
			
		}
		
		if(wi.X==BirdImage.x|| wi2.X==BirdImage.x) {
			score+=1;
		}
	}
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static boolean popUpMessage() {
		
		int result= JOptionPane.showConfirmDialog(null, "Game Over Lowperformer, Your score is "+score+"\n Do you want to restart the game?", "Game Over", JOptionPane.YES_NO_OPTION);
		
		if(result==JOptionPane.YES_OPTION) {
			return true;
		}else {
			return false;
		}
	}

}
