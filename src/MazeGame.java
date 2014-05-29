import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 * Maze Game
 * 
 * @author Gabriel
 */
public class MazeGame {
	public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		final FancyMaze3DComponent scene = new FancyMaze3DComponent();
		final CustomButton newGameButton = new CustomButton();
		newGameButton.setText("New Game");
		final CustomButton quitButton = new CustomButton();
		quitButton.setText("Quit");
		final CustomButton pauseButton = new CustomButton();
		pauseButton.setText("Unpause");
		final CustomButton audioButton = new CustomButton();
		audioButton.setText("Mute");
		
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scene.newGame();
				newGameButton.setEnabled(false);
				newGameButton.setVisible(false);
				quitButton.setEnabled(false);
				quitButton.setVisible(false);
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
				pauseButton.setEnabled(false);
				pauseButton.setVisible(false);
				audioButton.setEnabled(false);
				audioButton.setVisible(false);
				quitButton.setEnabled(false);
				quitButton.setVisible(false);
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
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("paused")) {
					if (evt.getNewValue().equals(true)) {
						pauseButton.setLocation(3 * scene.getWidth() / 8, 1 * scene.getHeight() / 8);
						pauseButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
						pauseButton.setEnabled(true);
						pauseButton.setVisible(true);
						audioButton.setLocation(3 * scene.getWidth() / 8, 3 * scene.getHeight() / 8);
						audioButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
						audioButton.setEnabled(true);
						audioButton.setVisible(true);
						quitButton.setLocation(3 * scene.getWidth() / 8, 5 * scene.getHeight() / 8);
						quitButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
						quitButton.setEnabled(true);
						quitButton.setVisible(true);
						frame.requestFocusInWindow();
					}
				} else if (evt.getPropertyName().equals("running")) {
					if (evt.getNewValue().equals(false)) {
						newGameButton.setLocation(3 * scene.getWidth() / 8, 1 * scene.getHeight() / 8);
						newGameButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
						newGameButton.setVisible(true);
						newGameButton.setEnabled(true);
						quitButton.setLocation(3 * scene.getWidth() / 8, 5 * scene.getHeight() / 8);
						quitButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
						quitButton.setEnabled(true);
						quitButton.setVisible(true);
						frame.requestFocusInWindow();
					}
				} else if (evt.getPropertyName().equals("muted")) {
					if (evt.getNewValue().equals(true)) {
						audioButton.setText("Unmute");
					} else {
						audioButton.setText("Mute");
					}
				}
			}
		});
		
		frame.add(newGameButton);
		frame.add(quitButton);
		frame.add(pauseButton);
		frame.add(audioButton);
		frame.add(scene, BorderLayout.CENTER);
		frame.setMinimumSize(new Dimension(800, 600));
		frame.pack();
		frame.setVisible(true);
		newGameButton.setLocation(3 * scene.getWidth() / 8, 1 * scene.getHeight() / 8);
		newGameButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
		newGameButton.setVisible(true);
		newGameButton.setEnabled(true);
		quitButton.setLocation(3 * scene.getWidth() / 8, 5 * scene.getHeight() / 8);
		quitButton.setSize(scene.getWidth() / 4, scene.getHeight() / 8);
		quitButton.setEnabled(true);
		quitButton.setVisible(true);
	}
}
