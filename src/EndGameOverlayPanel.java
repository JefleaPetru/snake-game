import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This panel appears after the player loses the game.
 */
public class EndGameOverlayPanel extends JPanel {
    
    public EndGameOverlayPanel(int score, SnakeGame mainGame, Music music) {

        final String buttonMusic = "Button press.wav";
        setLayout(new GridBagLayout()); // Use GridBagLayout to center content
        setOpaque(false);      // Transparent background

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(new Color(10, 20, 70, 150)); // Semi-transparent background

        JLabel messageLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerPanel.add(messageLabel);

        JLabel scoreLabel = new JLabel("Your High Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerPanel.add(scoreLabel);
        
        JButton restartButton = new JButton("Restart");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                music.playTrack(buttonMusic);
                mainGame.restartGame(restartButton);
            }
        });
        innerPanel.add(restartButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(innerPanel, gbc);
    }
}
