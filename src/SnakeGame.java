import javax.swing.*;

/**
 * This is the Snake Game. Use WASD buttons to naccigate the snake around the plaing field. 
 * Collect apples (red squares) by eating them.
 * After each eaten apple, an obstacle appears (yellow square).
 * The snake dies if it hits the border, itself, or an obstacle.
 * You can restart the game (and also see your high score) 
 * in the game over panel that appears afterwards.
 * To the right of the game field, you can see an apple and score counter.
 * Collect the required number of apples to spawn a black hole (black square).
 * Going through the black hole will launch the new universe.
 * In this universe, your snake will be bigger at the start and 
 * will have to collect more apples.
 * Afetr 3 universes, you have completed the main game.
 * In the panel that appears, you can choose to start a new, endless game after that.
 * 
 * 
 * Project group 23
 * @author Vadym Kazmin (2080338)
 * @author Petru Jeflea (2132389)
 * 
 */
public class SnakeGame {
    private JFrame frame;
    private GamePanel gamePanel;
    private EndGameOverlayPanel overlayPanel;
    private EndGamePanelWin winPanel; 
    private Music music;

    /**
     * Creates a frame and a panel for the game to be displayed.
     */
    public SnakeGame() {
        frame = new JFrame("Snake Game");
        music = new Music();
        gamePanel = new GamePanel(music, this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 635);
        frame.add(gamePanel);
        frame.setVisible(true);
    }

    /**
     * Called after the snake loses
     * to display the endgame panel.
     * 
     * @param score player's high score to be displayed
     */
    public void showEndGameOverlay(int score) {
        overlayPanel = new EndGameOverlayPanel(score, this, music);
        overlayPanel.setSize(frame.getSize());
        frame.getLayeredPane().add(overlayPanel, JLayeredPane.MODAL_LAYER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Called after the snake wins all 3 universes
     * to display the endgame panel.
     */
    public void showWinPanel() {
        winPanel = new EndGamePanelWin(this, music);
        winPanel.setSize(frame.getSize());
        frame.getLayeredPane().add(winPanel, JLayeredPane.MODAL_LAYER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Called when the player restarts a geme to remove the win or loss panel
     * and repaint the game field.
     * 
     * @param source the button user presses to restart
     */
    public void restartGame(JButton source) {

        if (source.getText().equals("Restart")) {
            frame.getLayeredPane().remove(overlayPanel);
        } else {
            frame.getLayeredPane().remove(winPanel);
        }
        gamePanel.loadNewUniverse();
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
