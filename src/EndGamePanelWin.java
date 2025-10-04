import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * This panel appears when the player has completed the 3 universes.
 */
public class EndGamePanelWin extends JPanel {
    
    public EndGamePanelWin(SnakeGame mainGame, Music music) {

        String buttonMusic = "Button press.wav";
        setLayout(new GridBagLayout()); // Use GridBagLayout to center content
        setOpaque(false);      // Transparent background

        JPanel innerPanel = new JPanel();

        
        innerPanel.setBackground(new Color(255, 215, 0)); // Semi-transparent background

        JLabel messageLabel = new JLabel("-You won-", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        innerPanel.add(messageLabel);
        
        JButton restartButtonFreeGame = new JButton("Restart Endless Game");
        restartButtonFreeGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButtonFreeGame.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                music.playTrack(buttonMusic);
                mainGame.restartGame(restartButtonFreeGame);
            }
        });
        innerPanel.add(restartButtonFreeGame);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(innerPanel, gbc);
    }
}