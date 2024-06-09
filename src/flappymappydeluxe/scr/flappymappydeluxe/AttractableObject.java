package flappymappydeluxe;

import java.awt.Rectangle;

public interface AttractableObject {

    boolean isVisible();

    void moveToPlayer(int x, int y);

    Rectangle getRect();

    int getX();

    int getY();

    void setX(int i);

    void setY(int i);

}

