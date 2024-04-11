import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(){
        super("Flappy Bird");
        setSize(600, 1000);
        setBackground(java.awt.Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        add(new Game());
        pack();
        setVisible(true);

    }

    public static void main(String[] args){
        new MainFrame();
    } 
}
