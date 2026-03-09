import javax.swing.JFrame;

public class Snake_Game extends JFrame {

    public Snake_Game() {
        super("Snake Game");

        add(new Board());

        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);   // <-- CENTER SCREEN
        setVisible(true);
    }

    public static void main(String[] args) {
        new Snake_Game();
    }
}
