import java.awt.BorderLayout;
import java.awt.Dimension;
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
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		final Maze3DComponent scene = new Maze3DComponent();
		final AudioButton audioButton = new AudioButton();
		
		frame.add(scene, BorderLayout.CENTER);
		frame.add(audioButton, BorderLayout.SOUTH);
		frame.setMinimumSize(new Dimension(672 + 16, 672 + 39));
		frame.setVisible(true);
	}
}
