import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {

    public Image bgPreScaled = new ImageIcon("flappy-bird-background.jpg").getImage();
    JLabel background;
    public Bird bird = new Bird();
    protected int score = -1;
    public JLabel scoreLabel = new JLabel("Score: " + score);
    protected final int birdCordX = 150;
    protected double birdCordY = 400 ;
    protected double speed = 3;
    protected final double maxSpeed = 20;
    protected double div = 1;
    protected double gravPull = 0;
    protected double maxGravPull = 10;
    protected double gravPullMultiplier = 1.6;
    protected double grabPullAddon = 2;
    protected double jumpHeight = -7;


    protected Timer FrameRate = new Timer(1000/60, new ActionListener(){
        public void actionPerformed(ActionEvent e){
            if(checkColision()) pause();
            checkPipeSpawnAndScore();
            checkPipeDespawn();
            movePipes();
            bird.setBounds(birdCordX - 34, (int)birdCordY - 20, 68, 40);
            birdCordY += gravPull;
            background.add(bird);
            repaint();
        }
    });
    protected Timer Speeder = new Timer((int) (3000 / div), new ActionListener(){
        public void actionPerformed(ActionEvent e){
            speed *= 1.1;
            if(speed > 8 ) {
                System.out.println("High speed mode activated!  Score: " + score);
                grabPullAddon = 3;
                gravPullMultiplier = 1.7;
                div = 0.5;
                jumpHeight = -9;
            }
            if(speed > maxSpeed) Speeder.stop();
        }
    });
    protected Timer Gravity = new Timer(160, new ActionListener(){
        public void actionPerformed(ActionEvent e){
            if(gravPull < 0) gravPull += 3;
            else if(gravPull < maxGravPull) {
                gravPull += grabPullAddon;
                gravPull *= gravPullMultiplier;
            }
        }
    });
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }
    public void keyReleased(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            gravPull = jumpHeight;
        }
    }

    protected java.util.Deque<Pipes> QueueOfUpperPipes = new java.util.LinkedList<Pipes>();
    protected java.util.Deque<Pipes> QueueOfBottomPipes = new java.util.LinkedList<Pipes>();
    public Game(){
        Image bgScaled = bgPreScaled.getScaledInstance(1400, 1000, Image.SCALE_SMOOTH);
        background = new JLabel(new ImageIcon(bgScaled));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        background.setBounds(0, 0, 1400, 1000);
        setPreferredSize(new Dimension(600, 1000));
        setLayout(null);
        add(background);
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        scoreLabel.setBounds(230, 100, 140, 30);
        background.add(scoreLabel);
        bird.setBounds(birdCordX - 34, (int)birdCordY - 20, 80, 80);
        background.add(bird);
        start();
    }


    public void start(){
        Speeder.start();
        Gravity.start();
        FrameRate.start();
    }

    public void reset(){
        birdCordY = 400;
        clearPipes();
        score = -1;
        updateScore();
        speed = 3;
        gravPull = 2;
        grabPullAddon = 2;
        gravPullMultiplier = 1.6;
        jumpHeight = -7;
        div = 1;

    }
    public void pause() {
        Speeder.stop();
        Gravity.stop();
        FrameRate.stop();
        int option = JOptionPane.showConfirmDialog(null, "Game Over! Czy chcesz zagrać ponownie?", "Koniec gry", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            reset();
            Gravity.start();
            Speeder.start();
            FrameRate.start();
        }
        if(option == JOptionPane.NO_OPTION) System.exit(0);
    }
    public void movePipes() {
        for (Pipes pipe : QueueOfUpperPipes)
            pipe.setX(pipe.getX() - (int)speed);
        for (Pipes pipe : QueueOfBottomPipes)
            pipe.setX(pipe.getX() - (int)speed);
    }
    public boolean checkColision(){
        if(birdCordY < 0 || birdCordY > 1000){
            System.out.println("Out of Bounds");
            return true;
        }

        int xColisionLeftBorder = birdCordX -10;
        int xColisionRightBorder = birdCordX + 25;
        int yColisionTopBorder = (int)birdCordY + 20;
        int yColisionBottomBorder = (int)birdCordY - 10;

        for (Pipes pipe : QueueOfUpperPipes){
            if(xColisionLeftBorder <= pipe.getX() + pipe.getWidth() && xColisionRightBorder >= pipe.getX()){
                if( yColisionTopBorder <= 420 - pipe.getOffset() * pipe.getMultiplier() + pipe.getGap() && yColisionBottomBorder >= pipe.getHeight())
                    return false;

                System.out.println("Bird Y: " + birdCordY + " Range: <" + pipe.getHeight() + ", " + (pipe.getHeight() + pipe.getGap()) + ">");
                return true;
            }
        }
        return false;
    }

    public void addPipe(){
        int offset = (int)(Math.random() * 150 + 50);
        int multiplier = (Math.random() > 0.5) ? 1 : -1;

        Pipes upperPipe = new Pipes(offset, multiplier,420 - offset*multiplier);
        upperPipe.setBackground(Color.YELLOW);
        upperPipe.setBounds(upperPipe.getX(), 0, 70, upperPipe.getHeight());
        QueueOfUpperPipes.add(upperPipe);
        background.add(upperPipe);

        Pipes bottomPipe = new Pipes(offset, multiplier, 420 + offset*multiplier );
        bottomPipe.setBackground(Color.GREEN);
        bottomPipe.setBounds(bottomPipe.getX(), 420 - offset*multiplier + bottomPipe.getGap(), 70, 1000 - bottomPipe.getY() );
        QueueOfBottomPipes.add(bottomPipe);
        background.add(bottomPipe);

        bottomPipe.setVisible(true);
        upperPipe.setVisible(true);
    }
    public void updateScore(){
        score++;
        scoreLabel.setText("Score: " + score);

    }
    public void checkPipeSpawnAndScore(){
        if(QueueOfUpperPipes.isEmpty() || (QueueOfUpperPipes.peekLast().getX() < 200 && QueueOfBottomPipes.peekLast().getX() < 200)){
                addPipe();
                updateScore();
            }
        }

    public void checkPipeDespawn(){
        if(!QueueOfUpperPipes.isEmpty() && QueueOfUpperPipes.peek().getX() < -70){
            Pipes poppedPipe = QueueOfUpperPipes.poll();
            poppedPipe.dropPipe();
            background.remove(poppedPipe);
            Pipes poppedPipe2 = QueueOfBottomPipes.poll();
            poppedPipe2.dropPipe();
            background.remove(poppedPipe2);
        }
    }

    public void clearPipes(){
        for (Pipes pipe : QueueOfUpperPipes){
            pipe.dropPipe();
            background.remove(pipe);
            QueueOfUpperPipes.remove(pipe);
        }
        for (Pipes pipe : QueueOfBottomPipes){
            pipe.dropPipe();
            background.remove(pipe);
            QueueOfBottomPipes.remove(pipe);
        }
    }
}