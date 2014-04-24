/**
 * Maze Interface
 *
 * @author AwolGerbil
 */

public interface Maze {
    /**
     * Generates a Maze
     *
     * @param height
     *            The height of the Maze
     * @param width
     *            The width of the Maze
     * 
     * @return The resulting Maze
     */
    public Maze generateMaze(int height, int width);
    
    /**
     * Returns whether there is a wall above given Coordinate
     * 
     * @param c
     *            The Coordinate
     * 
     * @return whether there is a wall above the Coordinate
     */
    public boolean wallUp(Coordinate c);
    
    /**
     * Returns whether there is a wall to the right of given Coordinate
     * 
     * @param c
     *            The Coordinate
     * 
     * @return whether there is a wall to the right of the Coordinate
     */
    public boolean wallRight(Coordinate c);
    
    /**
     * Returns whether there is a wall below given Coordinate
     * 
     * @param c
     *            The Coordinate
     * 
     * @return whether there is a wall below the Coordinate
     */
    public boolean wallDown(Coordinate c);
    
    /**
     * Returns whether there is a wall to the left of given Coordinate
     * 
     * @param c
     *            The Coordinate
     * 
     * @return whether there is a wall to the left of the Coordinate
     */
    public boolean wallLeft(Coordinate c);
}
