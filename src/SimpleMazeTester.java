import org.junit.Test;

/**
 * Tester for SimpleMaze
 * 
 * @author Gabriel
 */
public class SimpleMazeTester {
	@Test
	public void megaTest() {
		SimpleMaze maze = new SimpleMaze(10, 10);
		maze.drawMaze();
	}
}
