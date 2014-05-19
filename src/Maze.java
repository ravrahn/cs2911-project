/**
 * Maze Interface Note: (0, 0) is the top left
 * 
 * @author AwolGerbil
 */

public interface Maze {
	/**
	 * Returns whether there is a wall in the direction at the given Coordinate
	 * 
	 * @param c
	 *            The Coordinate
	 * @param direction
	 *            The direction
	 * @return Whether there is a wall in the direction at the given Coordinate
	 */
	public boolean getWall(Coordinate c, int direction);

	/**
	 * Returns the width of the maze
	 * 
	 * @return The width of the maze
	 */
	public int getWidth();

	/**
	 * Returns the height of the maze
	 * 
	 * @return The height of the maze
	 */
	public int getHeight();
	
	public static final Integer UP = 0;
	public static final Integer RIGHT = 1;
	public static final Integer DOWN = 2;
	public static final Integer LEFT = 3;
}
