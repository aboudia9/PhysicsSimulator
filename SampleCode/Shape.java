package PhysicsSimulator;
import java.awt.Color;
import java.awt.Graphics;

public class Shape{
    private int x;
    private int y;
    private Color blue;

    public Shape(){
        //random x within 0 to 798
        x = (int)(Math.random() * 799 );
        //random y within 0 to 595
        y = (int)(Math.random() * 596);
        blue = new Color (0,0,255);
    }
    public void drawMe(Graphics g){
        g.setColor(blue);
        g.fillOval(x,y,20,20);
    }

    public void moveRight(){
        x++;
        if (x>820){
            x = -25;
        }
    }
   
}