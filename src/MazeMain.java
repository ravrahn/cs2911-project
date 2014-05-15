import javax.swing.JFrame;


/**
 * 
 */

/**
 * @author ntpmtv
 *
 */
public class MazeMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame maze = new JFrame();
		MazeBoard mazeB = new MazeBoard();
		maze.setTitle("MazeGame");
		maze.add(mazeB);
		maze.setSize(800,600);
		maze.setVisible(true);
		
	}
	
}
