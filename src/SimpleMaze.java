/**
 * Simple Maze Implementation Class
 * 
 * @author Gabriel
 */
public class SimpleMaze implements Maze {
	/* (non-Javadoc)
	 * @see Maze#generateMaze(int, int)
	 */
	@Override
	public Maze generateMaze(int width, int height) throws IllegalArgumentException {
		if (width <= 0) {
			throw new IllegalArgumentException("Width > 0 pls");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Height > 0 pls");
		}
		this.width = width;
		this.height = height;
		top = new boolean[width][height];
		right = new boolean[width][height];
		bot = new boolean[width][height];
		left = new boolean[width][height];
		
		// TODO fix from empty maze
		return null;
	}

	/* (non-Javadoc)
	 * @see Maze#wallUp(Coordinate)
	 */
	@Override
	public boolean wallUp(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() > width || c.getY() < 0 || c.getY() > height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return top[c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallRight(Coordinate)
	 */
	@Override
	public boolean wallRight(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() > width || c.getY() < 0 || c.getY() > height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return right[c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallDown(Coordinate)
	 */
	@Override
	public boolean wallDown(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() > width || c.getY() < 0 || c.getY() > height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return bot[c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallLeft(Coordinate)
	 */
	@Override
	public boolean wallLeft(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() > width || c.getY() < 0 || c.getY() > height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return left[c.getX()][c.getY()];
	}
	
	/* (non-Javadoc)
	 * @see Maze#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}
	
	/* (non-Javadoc)
	 * @see Maze@getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}
	
	int width;
	int height;
	boolean[][] top;
	boolean[][] right;
	boolean[][] bot;
	boolean[][] left;
}
