package flappymappydeluxe;

public interface Enemy {
    
    
        void setVisible(boolean b);

        void spawn( WallImage wall);


        public void handleCollision();

}
