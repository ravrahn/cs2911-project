/**
 * Maze Interface
 * Note: (0, 0) is the top left
 * 
 * @author AwolGerbil
 */

public interface Maze {
	/**
	 * Returns whether there is a wall above given Coordinate
	 * 
	 * @param c
	 *            The Coordinate
	 * 
	 * @return Whether there is a wall above the Coordinate
	 */
	public boolean wallUp(Coordinate c);

	/**
	 * Returns whether there is a wall to the right of given Coordinate
	 * 
	 * @param c
	 *            The Coordinate
	 * 
	 * @return Whether there is a wall to the right of the Coordinate
	 */
	public boolean wallRight(Coordinate c);

	/**
	 * Returns whether there is a wall below given Coordinate
	 * 
	 * @param c
	 *            The Coordinate
	 * 
	 * @return Whether there is a wall below the Coordinate
	 */
	public boolean wallDown(Coordinate c);

	/**
	 * Returns whether there is a wall to the left of given Coordinate
	 * 
	 * @param c
	 *            The Coordinate
	 * 
	 * @return Whether there is a wall to the left of the Coordinate
	 */
	public boolean wallLeft(Coordinate c);

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
}
