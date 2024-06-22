package flappymappydeluxe;

import java.awt.Graphics;

public interface Enemy {
    
    
        void setVisible(boolean b);

        void spawn( WallImage wall);


        public void handleCollision();

        void drawPowerUp(Graphics g);

        void moveEnemy(WallImage wi);

}
