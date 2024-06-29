package flappymappydeluxe;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Button {
    
    private int x, y, width, height; // Button coordinates and size
    private String text; // Button text
    private Color textColor; // Text color
    private Font font; // Text font
    private Image buttonImage; // Button image
    
    // Button constructor
    public Button(int x, int y, int width, int height, String text, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.textColor = Color.BLACK;
        this.font = new Font("Minecraft", Font.BOLD, 16); // Set to a Minecraft-style font if available
        try {
            this.buttonImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            this.buttonImage = null; // Fallback in case the image doesn't load
        }
    }
    
    // Draw method visualizes the button under the specified coordinates and size
    public void draw(Graphics g) {
        if (buttonImage != null) {
            // Draw the button image
            g.drawImage(buttonImage, x, y, width, height, null);
        } else {
            // Fallback to a default rectangle if the image is not available
            g.setColor(Color.white);
            g.fillRect(x, y, width, height);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(x, y, width, height);
        }
        
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
