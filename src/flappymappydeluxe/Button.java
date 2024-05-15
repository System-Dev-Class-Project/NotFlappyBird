package flappymappydeluxe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;




/**
 * The button class introduces buttons, which when called should create instances of clickable buttons 
 * in other classes like the MenuPanel Class. The functionality of the class consists of transferring the user to 
 * the desired window (Playing the game, Achievements, Options, etc.)
 * x and y are the window coordinates which determine where the button should be placed inside the window.
 * width and height determine the size of the button
 * the specified text will appear inside the button
 */
public class Button {
	
	private int x, y, width, height;  
	private String text;
	private Color bgColor;
    private Color textColor;
    private Font font;
    
	//button constructor
	public Button (int x, int y, int width, int height,String text) { 
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.text=text;
		//this.bgColor= new Color(0,0,0,0); //transparent
		this.bgColor= Color.LIGHT_GRAY;
		this.textColor = Color.BLACK;
		this.font= new Font ("Arial", Font.BOLD, 16);
	}
	//draw method visualizes the button under the specified coordinates and size
	public void draw (Graphics g) {
		
		g.setColor(bgColor);
        g.fillRect(x, y, width, height);
        g.setColor(textColor);
        g.setFont(font);
		g.drawRect(x,y,width,height);            
		g.drawString(text, x+10, y+height/2);  //We decided to use not x and y as is, that created visual issues with the text not being completely inside the button
	}
	
	//getBound method returns the type rectangle of the specified button size and location 
	public Rectangle getBounds() {
		return new Rectangle (x,y,width, height);
	}
	//isClicked method tests (therefore boolean) whether the mouse click event happens in a specified button
	//This is why we needed to create a getBounds method that returns the type rectangle because we need to check if the rectangle contains a specified point, in this case the mouse click location
	public boolean isClicked(MouseEvent e) {
		return getBounds().contains(e.getPoint());
	}
	

}
