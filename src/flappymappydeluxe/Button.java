package flappymappydeluxe;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Button {
    
    private int x, y, width, height;
    private String text;
    private Color bgColor;
    private Color textColor;
    private Font font;
    
    // Button constructor
    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.bgColor = Color.LIGHT_GRAY; // Minecraft-like color
        this.textColor = Color.BLACK;
        this.font = new Font("Minecraft", Font.BOLD, 16); // Set to a Minecraft-style font if available
    }
    
    // Draw method visualizes the button under the specified coordinates and size
    public void draw(Graphics g) {
        // Set the color for the button background
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);
        
        // Set the color for the button border
        g.setColor(Color.BLACK); // Minecraft buttons have black borders
        g.drawRect(x, y, width, height);
        
        // Calculate the position to center the text
        FontMetrics metrics = g.getFontMetrics(font);
        int textX = x + (width - metrics.stringWidth(text)) / 2;
        int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        
        // Set the color and font for the text
        g.setColor(textColor);
        g.setFont(font);
        
        // Draw the centered text
        g.drawString(text, textX, textY);
    }
    
    // GetBounds method returns the type rectangle of the specified button size and location
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // IsClicked method tests (therefore boolean) whether the mouse click event happens in a specified button
    public boolean isClicked(MouseEvent e) {
        return getBounds().contains(e.getPoint());
    }
}
