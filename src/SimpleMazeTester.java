import java.util.ArrayList;

import org.junit.Test;

/**
 * Tester for SimpleMaze
 * 
 * @author Gabriel
 */
public class SimpleMazeTester {
	@Test
	public void megaTest() {
		SimpleMaze maze = new SimpleMaze(5, 5);
		maze.drawMaze();
		ArrayList<Wall> lines = maze.toWalls();
		for (Wall l : lines) {
			System.out.println(l);
		}
	}
}
