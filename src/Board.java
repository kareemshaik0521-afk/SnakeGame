import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class Board extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;
    private final int alldots = 900;
    private final int dotsize = 10;
    private final int[] x = new int[900];
    private final int[] y = new int[900];
    private final int rand = 29;
    private int apple_x;
    private int apple_y;
    private int dots;
    private Timer timer;
    private boolean right = true;
    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
    private boolean ingame = true;
    private boolean paused = false;   // ✅ ADDED
    private int score = 0;

    Board() {
        setFocusable(true);
        addKeyListener(new TAdapter());
        this.setBackground(Color.BLACK);
        this.loadimages();
        this.initgame();
    }

    public void loadimages() {
        apple = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/icons/apple.png"))
        ).getImage();
        dot = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/icons/dot.png"))
        ).getImage();
        head = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/icons/head.png"))
        ).getImage();
    }

    public void initgame() {
        this.dots = 3;
        score = 0;
        ingame = true;
        paused = false;

        for(int i = 0; i < this.dots; ++i) {
            this.y[i] = 50;
            this.x[i] = 50 - i * 10;
        }

        locateapple();

        timer = new Timer(140, this);  //
        timer.start();
    }


    public void locateapple(){
        int r=(int)(Math.random()*rand);
        apple_x=r*dotsize;
        r=(int)(Math.random()*rand);
        apple_y=r*dotsize;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g) {
        if (ingame) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("sansSerif", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);

            if (paused) {
                drawCenteredText(g, "PAUSED");
                return;
            }

            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < this.dots; ++i) {
                if (i == 0) {
                    g.drawImage(this.head, this.x[i], this.y[i], this);
                } else {
                    g.drawImage(this.dot, this.x[i], this.y[i], this);
                }
            }
        } else {
            gameover(g);
        }
    }

    public void gameover(Graphics g) {
        drawCenteredText(g, "GAME OVER");
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(
                "SCORE: " + score,
                (getWidth() - g.getFontMetrics().stringWidth("SCORE: " + score)) / 2,
                getHeight() / 2 + 30
        );
        g.drawString(
                "Press R to Restart",
                (getWidth() - g.getFontMetrics().stringWidth("Press R to Restart")) / 2,
                getHeight() / 2 + 60
        );
    }

    // ✅ CENTER TEXT HELPER (SMALL, SAFE)
    private void drawCenteredText(Graphics g, String text) {
        Font font = new Font("SansSerif", Font.BOLD, 22);
        FontMetrics fm = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(
                text,
                (getWidth() - fm.stringWidth(text)) / 2,
                getHeight() / 2
        );
    }

    public void move(){
        for(int i=dots;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(right){ x[0]+=dotsize; }
        if(left){ x[0]-=dotsize; }
        if(up){ y[0]-=dotsize; }
        if(down){ y[0]+=dotsize; }
    }

    public void checkapple(){
        if(x[0]==apple_x && y[0]==apple_y){
            dots++;
            score++;
            locateapple();
        }
    }

    public void checkcollision(){
        for(int i=dots;i>0;i--){
            if ((i>4)&& x[0]==x[i] && y[0]==y[i]){
                ingame=false;
            }
        }
        if (x[0]>=300 || y[0]>=300 || x[0]<0 || y[0]<0){
            ingame=false;
        }
        if(!ingame){
            timer.stop();
        }
    }

    public void actionPerformed(ActionEvent e){
        if (!paused && ingame) {
            checkapple();
            checkcollision();
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right) {
                left = true; up = down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true; up = down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true; left = right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true; left = right = false;
            }

            // ✅ PAUSE
            if (key == KeyEvent.VK_SPACE) {
                paused = !paused;
            }

            // ✅ RESTART
            if (key == KeyEvent.VK_R && !ingame) {
                timer.start();
                initgame();
            }
        }
    }
}
