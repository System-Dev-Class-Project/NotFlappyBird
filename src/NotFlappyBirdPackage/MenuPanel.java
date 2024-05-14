package NotFlappyBirdPackage;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	
	
	
	private static final long serialVersionUID=1L;
	
	private BufferedImage img=null;
	//switch screens
	public boolean StartingPoint= false;
	
	
	public MenuPanel() {
	    LoadImage();
	    // Handle a mouse click event
	    this.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            super.mouseClicked(e);
	            StartingPoint = true;
	        }
	    });
	}
	
	
	private void LoadImage() {
		
		try {
			img = ImageIO.read(new File("Images\\Menu.png"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.drawImage(img, 0, 0 ,GamePanel.WIDTH, GamePanel.HEIGHT, null);
		
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
