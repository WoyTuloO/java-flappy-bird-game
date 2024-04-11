import javax.swing.*;
import java.awt.*;

public class Bird extends JLabel {
    public Image birdImage = new ImageIcon("flappy-bird-bird.png").getImage();
    public Bird(){
        Image birdScaled = birdImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        setBackground(java.awt.Color.RED);
        setIcon(new ImageIcon(birdScaled));
        setVisible(true);
    }
}
