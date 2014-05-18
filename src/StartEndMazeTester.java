import org.junit.Test;

/**
 * Tester for StartEndMaze
 * 
 * @author Gabriel
 */
public class StartEndMazeTester {
	@Test
	public void megaTest() {
		StartEndMaze maze = new StartEndMaze(20, 20,
				new SimpleCoordinate(1, 1), new SimpleCoordinate(18, 18));
		maze.drawMaze();
	}
}
