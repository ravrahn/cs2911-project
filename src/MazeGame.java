import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
 * @author Gabriel, Owen
 */
public class MazeGame {

	public static void main(String[] args) throws IOException,
			UnsupportedAudioFileException, LineUnavailableException {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel gamePane = new JPanel();
		gamePane.setLayout(new BorderLayout());

		frame.setContentPane(gamePane);

		final JPanel menuPane = new MenuPanel();
		menuPane.setBounds(0, 0, 800, 600);
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.X_AXIS));

		final FancyMaze3DComponent scene = new FancyMaze3DComponent();
		
		// Add Game Keyboard
		KeyboardInput keyboard = new KeyboardInput();
		frame.addKeyListener(keyboard);

		// Add Generic Keyboard Listener for pause and mute and exit
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_P) {
					scene.resume();
					menuPane.setEnabled(false);
					menuPane.setVisible(false);
				} else if (e.getKeyCode() == KeyEvent.VK_M) {
					if (scene.isMuted()) {
						scene.unMute();
					} else {
						scene.mute();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});

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

		final JButton helpButton = new JButton();
		helpButton.setPreferredSize(new Dimension(128, 64));
		helpButton.setMinimumSize(new Dimension(128, 64));
		helpButton.setMaximumSize(new Dimension(128, 64));
		helpButton.setBackground(new Color(0xededed));
		helpButton.setFont(new Font("Roboto Condensed", Font.PLAIN, 24));
		helpButton.setText("Help");
		helpButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		helpButton.setEnabled(true);
		helpButton.setVisible(true);

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
				// stop showing the menu
				menuPane.setEnabled(false);
				menuPane.setVisible(false);
				// disable the main menu buttons
				newGameButton.setEnabled(false);
				newGameButton.setVisible(false);
				helpButton.setEnabled(false);
				helpButton.setVisible(false);
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
						helpButton.setEnabled(false);
						helpButton.setVisible(false);
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
					} else {
					}
				} else if (event.getPropertyName().equals("muted")) {
					if (event.getNewValue().equals(true)) {
						audioButton.setText("Unmute");
					} else {
						audioButton.setText("Mute");
					}
				} else if (event.getPropertyName().equals("winning")) {
					newGameButton.setEnabled(true);
					newGameButton.setVisible(true);
					pauseButton.setEnabled(false);
					pauseButton.setVisible(false);
					helpButton.setEnabled(true);
					helpButton.setVisible(true);
					audioButton.setEnabled(false);
					audioButton.setVisible(false);
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

		// top buttons
		panel.add(newGameButton);
		panel.add(pauseButton);
		panel.add(Box.createVerticalGlue());
		// middle buttons
		panel.add(helpButton);
		panel.add(audioButton);
		panel.add(Box.createVerticalGlue());
		// bottom buttons
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
