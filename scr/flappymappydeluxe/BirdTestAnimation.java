package flappymappydeluxe;



	import java.awt.Graphics;
	import java.awt.Rectangle;
	import java.awt.image.BufferedImage;
	import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
	import javax.swing.JFrame;
	/**
	 * The following class is the first test for simple animations for the bird, based on two separate images
	 * This class mirrors the original BirdImage in concept, but introduces new variables into existing methods to switch between
	 * two images of the bird, one with it's wings down and one with it's wings up.
	 * The variables and methods used are described separately.
	 */
	public class BirdTestAnimation {

	    private BufferedImage[] frames; // Array to hold bird frames
	    private int currentFrameIndex; // Index of the current frame
	    private int animationSpeed = 8; // Adjust this to control the speed of animation
	    private int frameCounter = 0; // Counter to control animation speed

	    private static int bird_dia = 36; 
		private static int mushroom_dia = 10;
    	private boolean mushroom = false;
	    public static int x = (GamePanel.WIDTH / 2) - bird_dia / 2;
	    public static int y = GamePanel.HEIGHT / 2; // Spawns the bird in the middle of the window

	    private AudioPlayer audioPlayer;
	    private static int speed = 2;
	    private int accel = 1;

	    public BirdTestAnimation() {
	        LoadImages(mushroom);
	        currentFrameIndex = 0;
			this.x=x;
			this.y=y;
	    }

	    private void LoadImages(boolean mushroom) {
			this.mushroom = mushroom;
			 if (mushroom) {
				bird_dia = mushroom_dia;
				// Load the smaller bird images
				frames = new BufferedImage[2];
				try {
					frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap-small.png"));
					frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap-small.png"));
				} catch (IOException e) {
					System.out.println("small bird not found");
					e.printStackTrace();
				}
			} else {
				bird_dia = 36;
				// Load the normal bird images
				frames = new BufferedImage[2];
				try {
					frames[0] = ImageIO.read(new File("NotFlappyBird-main/Images/redbird-midflap.png"));
					frames[1] = ImageIO.read(new File("NotFlappyBird-main/Images/redbirdnewflap.png"));
				} catch (IOException e) {
					System.out.println("bird not found");
					e.printStackTrace();
				}
			}

	    }

	    public void drawBird(Graphics g) { 
	        g.drawImage(frames[currentFrameIndex], x, y, null);
	    }
	    
	    public void setBirdImages(BufferedImage birdImage1, BufferedImage birdImage2) { //for the later bought shop skins, so that both new bird image 
	        frames[0] = birdImage1;
	        frames[1] = birdImage2;
	    }
	    
	    public void birdMovement(AudioPlayer audioPlayer) {
	    	 if (y >= 0 && y <= GamePanel.HEIGHT) {
	             speed += accel;
	             y += speed;
	         } else {
	        	audioPlayer.play("NotFlappyBird-main/Music/GameOver_sound.wav");
	        	audioPlayer.play("NotFlappyBird-main/Music/hurt_sound.wav");
	         	int option = GamePanel.popUpMessage();

	             if (option == 0) {  //reset game if yes is clicked
	                 try {
	                     Thread.sleep(500);
	                 } catch (Exception ex) {
	                     ex.printStackTrace();
	                 }
	                 reset();
	             } else if (option == 2) {    //close game if no is clicked
	             	 JFrame frame = FlappyClass.getWindow();
	                  MenuPanel.audioPlayer.stop();    //stops the music from playing when no is pressed after falling out of bounds. Also inserted in the wallImage class in the case of wall collision
	                  frame.dispose();
	                  FlappyClass.timer.stop();
	             }
	             else {    //last possible option is to go back to main menu 
	                 // Go back to main menu
	             	MenuPanel.switchMusic("NotFlappyBird-main/1-01. Main Theme (Title Screen).wav");
	             	reset();
	             	FlappyClass.timer.stop();
	                FlappyClass.cardLayout.show(FlappyClass.mainPanel, "menu");
	             }
	         }
	         animate(); // Call the animate method to update the bird's animation frame
	     }

	    public void goUpwards() {
	        speed = -17;
	    }

	    public static Rectangle getBirdRect() {
	        return new Rectangle(x, y, bird_dia, 35);
	    }

	    private void animate() {
	        frameCounter++;
	        if (frameCounter >= animationSpeed) {
	            // Toggle between frames
	            currentFrameIndex = (currentFrameIndex + 1) % frames.length;
	            frameCounter = 0;
	        }
	    }

	    public static void reset() {
	        speed = 2;
	        y = GamePanel.HEIGHT /2;
	        GamePanel.GameOver = true;
	        GamePanel.score = 0;
	    }

		public int getX() {
			// TODO Auto-generated method stub
			return x;
		}

		public boolean isMushroom() {
			return mushroom;
		}

		public void setMushroom(boolean b){
			mushroom = b;
			LoadImages(mushroom);
		}

		public int getDiameter() {
			// TODO Auto-generated method stub
			return bird_dia;
		}

        public int getWidth() {
            // TODO Auto-generated method stub
            return 36;
        }

        public int getHeight() {
            // TODO Auto-generated method stub
           return 36;
        }

        public int getY() {
            // TODO Auto-generated method stub
           return y;
        }
	}
	
	
	
	
	

