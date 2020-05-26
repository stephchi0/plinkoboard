import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
public class PlinkoBoard extends JPanel implements ActionListener, MouseListener
{
    int circleX = -50;
    int circleY = -50;
    int column = -5;
    int row = -5;
    boolean animation = false;
    boolean drop = false;
    
    int numberOfColumns = 7;
    int numberOfRows = 11;
    
    Timer timer = new Timer(20, this);
    
    boolean ready = false;
    Image khaled;
    Image moneyMaker;
    
    double[] multipliers = {0.5, 0.5, 2, 3, 2, 0.5, 0.5};
    public void setup()//sets up images and background mussiqq and st00f
    {
        if (!ready)
        {
            ready = true;
            try
            {
                khaled = ImageIO.read(getClass().getResource("liv.png"));
                moneyMaker = ImageIO.read(getClass().getResource("ded.png"));
            }
            catch (Exception e){}
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        //setting stuff up
        setup();
        
        //background
        setBackground(Color.white);
        
        //border
        g.drawRect(25, 50, 360, 600);
        
        //drawing points
        for (int i = 0; i < numberOfColumns; i++) {//7 columns
            //11 alternating rows
            for (int k = 0; k < numberOfRows; k++) {
                if (k % 2 == 0 && i < 6) {//odd rows
                    g.fillOval(i * 50 + 75, k * 50 + 75, 10, 10);
                }
                else if (k % 2 == 1) {//even rows
                    g.fillOval(i * 50 + 50, k * 50 + 75, 10, 10);
                    //drawing triangles on edge
                    int[] triX1 = {25, 55, 25};
                    int[] triX2 = {385, 355, 385};
                    int[] triY = {50 * k + 45, 50 * k + 80, 50 * k + 115};
                    g.fillPolygon(triX1, triY, 3);
                    g.fillPolygon(triX2, triY, 3);
                }
            }
        }
        
        //drawing khaled's beautiful face
        g.drawImage(khaled,//5, 8
        140, 410, 170, 440,
        0, 0, 420, 504,
        this);
        g.drawImage(khaled,//9, 8
        240, 410, 270, 440,
        420, 0, 0, 504,
        this);
        
        //drop spots
        for (int i = 0; i < numberOfColumns - 1; i++) {
            //holes in top
            g.setColor(Color.white);
            g.fillRect(i * 50 + 65, 30, 30, 30);
            
            //arrows
            g.setColor(Color.pink);
            int[] xs = {65 + i * 50, 95 + i * 50, 80 + i * 50};
            int[] ys = {10, 10, 40};
            g.fillPolygon(xs, ys, 3);
        }
        
        //dropping thing
        g.setColor(Color.orange);
        g.fillOval(circleX, circleY, 20, 20);
        
        //bottom spots
        for (int i = 0; i < multipliers.length; i++) {
            g.setColor(Color.green);
            g.fillRect(i * 50 + 40, 60 + numberOfRows * 50, 30, 20);
            g.setColor(Color.black);
            g.drawRect(i * 50 + 40, 60 + numberOfRows * 50, 30, 20);
            g.drawString(Double.toString(multipliers[i]) + "x", i * 50 + 41, 75 + numberOfRows * 50);
        }
        
        //if you hit khaled
        if (hitKhaled() && !animation) {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.setColor(Color.red);
            //draw khaled ded
            g.drawImage(moneyMaker,
            100, 90, 300, 290,
            0, 0, 934, 896,
            this);
            g.drawString("YOU HIT THE MONEY MAKER", 25, 315);
        }
    }
    int columnP = -1;
    public void actionPerformed(ActionEvent e) {
        if (animation) animate();
        repaint();
    }
    public void mousePressed(MouseEvent e) {
        if (!animation) {
            int x = e.getX();
            int y = e.getY();
            for (int i = 0; i < 6; i++) {
                if (x > i * 50 + 60 && x < i * 50 + 100 && y > 10 && y < 40) {
                    column = 2 * (i + 1);
                    row = 1;
                    circleX = (column * 25) - 5 + 25;
                    circleY = (row * 50) - 20 + 25 - 50;
                    animation = true;
                    drop = false;
                    timer.start();

                    break;
                }
            }
        }
    }
    String direction = "";
    int move0 = 50;
    int move1 = 15;
    int move2 = 50;
    String[] path = {"left", "right"};
    public void animate() {
        if (!drop) {
            direction = randomDirection();
            move(direction);
            drop = true;
            move0 = 50;
            move1 = 15;
            move2 = 50;
        }
        else if (move0 > 1 && row == 2) {
            move0-=5;
            circleY+=5;
        }
        else if (move1 > 1) {
            move1-=5;
            if (direction.equalsIgnoreCase("left"))
                circleX-=5;
            else
                circleX+=5;
        }  
        else if (move2 > 1) {
            move2-=5;
            circleY+=5;
            if (move2 % 5 == 0) {
                if (direction.equalsIgnoreCase("left"))
                    circleX--;
                else
                    circleX++;
            }
        }
        else
            drop = false;
    }
    public void move(String direction) {
        //if at bottom
        if (row > numberOfRows || hitKhaled()) {
            animation = false;
            drop = false;
            timer.stop();
        }
        else {
            //move according to direction
            switch (direction) {
                case "left":
                    column--;
                    break;
                case "right":
                    column++;
                    break;
            }
            row++;
        }
    }
    public String randomDirection() {
        //if on edges
        if (column == 1)
            return "right";
        if (column == numberOfColumns * 2 - 1)
            return "left";
        
        //50% chance of going left or right
        if (Math.random() < 0.5)
            return "left";
        return "right";
    }
    public boolean hitKhaled() {
        if (column == 5 && row == 8 || column == 9 && row == 8) {
            return true;
        }
        return false;
    }
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
