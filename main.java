import javax.swing.*;
import java.awt.event.*;
public class main
{
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setSize(450, 700);
        window.setResizable(false);
        
        PlinkoBoard board = new PlinkoBoard();
        window.add(board);
        board.addMouseListener(board);
        window.addKeyListener(board);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}