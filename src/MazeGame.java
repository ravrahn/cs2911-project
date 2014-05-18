import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * 
 * 
 * @author Gabriel
 */
public class MazeGame {
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MazeComponent scene = new MazeComponent();

		frame.add(scene);
		frame.setMinimumSize(new Dimension(672 + 16, 672 + 39));
		frame.setVisible(true);
	}
}
