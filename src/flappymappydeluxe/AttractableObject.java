package flappymappydeluxe;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface AttractableObject {

    boolean isVisible();

    void moveToPlayer(int x, int y);

    Rectangle getRect();

    int getX();

    int getY();

    void setX(int i);

    void setY(int i);

    void setVisible(boolean b);

    void spawn( WallImage wall);

    void drawPowerUp(Graphics g);

    void movePowerUp(WallImage wi, BirdTestAnimation bi);

}
