import org.junit.Test;

/**
 * Tester for SimpleMaze
 * 
 * @author Gabriel
 */
public class SimpleMazeTester {
	@Test
	public void megaTest() {
		SimpleMaze maze = new SimpleMaze(20, 20);
		maze.drawMaze();
	}
}
