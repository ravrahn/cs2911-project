import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Maze Game
 * 
 * @author Gabriel
 */
public class MazeGame {
	
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JPanel gamePane = new JPanel();
		gamePane.setLayout(new BorderLayout());
		
		frame.setContentPane(gamePane);
		
		final JPanel menuPane = new MenuPanel();
		menuPane.setBounds(0, 0, 800, 600);
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.X_AXIS));
		
		final FancyMaze3DComponent scene = new FancyMaze3DComponent();
		
		final JButton newGameButton = new JButton();
		newGameButton.setText("Play");
		newGameButton.setPreferredSize(new Dimension(128, 64));
		newGameButton.setMinimumSize(new Dimension(128, 64));
		newGameButton.setMaximumSize(new Dimension(128, 64));
		newGameButton.setBackground(new Color(0xededed));
		newGameButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 24));
		newGameButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		newGameButton.setEnabled(true);
		newGameButton.setVisible(true);
		
		final JButton pauseButton = new JButton();
		pauseButton.setPreferredSize(new Dimension(128, 64));
		pauseButton.setMinimumSize(new Dimension(128, 64));
		pauseButton.setMaximumSize(new Dimension(128, 64));
		pauseButton.setBackground(new Color(0xededed));
		pauseButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 24));
		pauseButton.setText("Unpause");
		pauseButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		pauseButton.setEnabled(false);
		pauseButton.setVisible(false);
		
		final JButton audioButton = new JButton();
		audioButton.setPreferredSize(new Dimension(128, 64));
		audioButton.setMinimumSize(new Dimension(128, 64));
		audioButton.setMaximumSize(new Dimension(128, 64));
		audioButton.setBackground(new Color(0xededed));
		audioButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 24));
		audioButton.setText("Mute");
		audioButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		audioButton.setEnabled(false);
		audioButton.setVisible(false);
		
		final JButton quitButton = new JButton();
		quitButton.setPreferredSize(new Dimension(128, 64));
		quitButton.setMinimumSize(new Dimension(128, 64));
		quitButton.setMaximumSize(new Dimension(128, 64));
		quitButton.setBackground(new Color(0xededed));
		quitButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 24));
		quitButton.setText("Quit");
		quitButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		quitButton.setEnabled(true);
		quitButton.setVisible(true);
		
		
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.newGame();
				menuPane.setEnabled(false);
				menuPane.setVisible(false);
				newGameButton.setEnabled(false);
				newGameButton.setVisible(false);
			}
		});
		
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.resume();
				menuPane.setEnabled(false);
				menuPane.setVisible(false);
			}
		});
		
		audioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (scene.isMuted()) {
					scene.unMute();
				} else {
					scene.mute();
				}
			}
		});
		
		scene.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getPropertyName().equals("paused")) {
					if (event.getNewValue().equals(true)) {
						pauseButton.setEnabled(true);
						pauseButton.setVisible(true);
						audioButton.setEnabled(true);
						audioButton.setVisible(true);
						menuPane.setEnabled(true);
						menuPane.setVisible(true);
						frame.requestFocusInWindow();
					}
				} else if (event.getPropertyName().equals("running")) {
					if (event.getNewValue().equals(false)) {
						menuPane.setEnabled(true);
						menuPane.setVisible(true);
						frame.requestFocusInWindow();
					}
				} else if (event.getPropertyName().equals("muted")) {
					if (event.getNewValue().equals(true)) {
						audioButton.setText("Unmute");
					} else {
						audioButton.setText("Mute");
					}
				}
			}
		});
		
		JLabel titleLabel = new JLabel("Maze");
		titleLabel.setBorder(new EmptyBorder(0, 28, 0, 28));
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Roboto Slab", Font.PLAIN, 96));
		menuPane.add(titleLabel);
		
		menuPane.add(Box.createHorizontalGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(128, menuPane.getHeight()));
		panel.setOpaque(false);
		panel.add(Box.createVerticalGlue());
		
		panel.add(newGameButton);
		panel.add(pauseButton);
		panel.add(Box.createVerticalGlue());
		panel.add(audioButton);
		panel.add(Box.createVerticalGlue());
		panel.add(quitButton);
		panel.add(Box.createVerticalGlue());
		
		menuPane.add(panel);
		menuPane.add(Box.createHorizontalGlue());
		
		gamePane.add(menuPane);
		gamePane.add(scene, BorderLayout.CENTER);
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}
