package flappymappydeluxe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class FlappyClass {

	//window
	//1st Panel -> Main Menu
	//2nd Panel -> Actual game
	private static JFrame window;
	public static Timer timer;
	
	private FlappyClass() {
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminates the program after window is closed
		window.setSize(GamePanel.WIDTH, GamePanel.HEIGHT); //width, height
		window.setLocationRelativeTo(null); //sets window pop-up location to screen center
		window.setTitle("Not Flappy Bird"); //title
		window.setResizable(true);  //prohibits window resizing
		
	}
	
	private void rendering() {
		
		MenuPanel mp = new MenuPanel();
		GamePanel gp = new GamePanel();
		
		timer = new Timer(20,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gp.repaint(); //its calling the paint method iteratively
				gp.Move();
				mp.repaint();
				mp.MenuMove();
			
			}
		});
		
		window.add(mp);
		window.setVisible(true);
		while (mp.StartingPoint==false) {   //code loops until user clicks a button
			try {
				Thread.sleep(5);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		window.remove(mp);  //removal of menu panel and switch to game panel, after click has been passed
		window.add(gp);
		gp.setVisible(true);
		window.revalidate();
		
		timer.start();
	}
	
	
	
	public static JFrame getWindow() {
		return window;
		
	}
	
	
	
	public static void main(String [] args) {
		
		FlappyClass fc = new FlappyClass();
		
		fc.rendering();
		
	}
	
	}
	
	
	
	
	
	
	
	

