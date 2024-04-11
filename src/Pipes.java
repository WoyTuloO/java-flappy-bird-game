import javax.swing.*;


public class Pipes extends JPanel {
    private final int width = 70;
    private final int height;
    private final int gap = 220;
    private int x = 600;

    private final int multiplier;
    private final int offset;
    public Pipes(int multiplier,int offset, int height){
        this.multiplier = multiplier;
        this.offset = offset;
        this.height = height;
    }

    public void setX(int x) { this.x = x; }

    public int getWidth(){
        return width;
    }

    public int getHeight(){return height;}

    public int getX(){
        return x;
    }
    public int getMultiplier(){return multiplier;}
    public int getOffset(){return offset;}
    public int getGap(){return gap; }
    public void dropPipe(){
        this.setVisible(false);
    }
}
