import java.util.ArrayList;
import java.util.Random;

/**
 * Simple Maze Implementation Class
 * 
 * @author Gabriel
 */
public class SimpleMaze implements Maze {
	public SimpleMaze(int width, int height) {
		if (width <= 0) {
			throw new IllegalArgumentException("Width > 0 pls");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Height > 0 pls");
		}
		this.width = width;
		this.height = height;
		edges = new boolean[4][width][height];
		
		// Setting up for Randomised Prim's
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < height; k++) {
					edges[i][j][k] = true;
				}
			}
		}
		
		Random random = new Random();
		boolean[][] inMaze = new boolean[width][height];
		Coordinate c = new SimpleCoordinate(0, 0);
		ArrayList<Coordinate> openSet = new ArrayList<Coordinate>();
		openSet.add(c);
		
		ArrayList<Integer> edgeOptions;
		ArrayList<Integer> cellOptions;
		int edge;
		
		while (!openSet.isEmpty()) {
			c = openSet.remove(random.nextInt(openSet.size()));
			inMaze[c.getX()][c.getY()] = true;
			
			// Array of possible edges to add
			edgeOptions = new ArrayList<Integer>();
			// Array of cells to expand to
			cellOptions = new ArrayList<Integer>();
			for (int i = 0; i < 4; i++) cellOptions.add(i);
			
			// Consider top edge if possible
			if (c.getY() - 1 < 0) {
				cellOptions.remove(TOP);
			} else if (inMaze[c.getX()][c.getY() - 1]) {
				edgeOptions.add(TOP);
				cellOptions.remove(TOP);
			}
			// Consider right edge if possible
			if (c.getX() + 1 >= width) {
				cellOptions.remove(RIGHT);
			} else if (inMaze[c.getX() + 1][c.getY()]) {
				edgeOptions.add(RIGHT);
				cellOptions.remove(RIGHT);
			}
			// Consider bottom edge if possible
			if (c.getY() + 1 >= height) {
				cellOptions.remove(BOT);
			} else if (inMaze[c.getX()][c.getY() + 1]) {
				edgeOptions.add(BOT);
				cellOptions.remove(BOT);
			}
			// Consider left edge if possible
			if (c.getX() - 1 < 0) {
				cellOptions.remove(LEFT);
			} else if (inMaze[c.getX() - 1][c.getY()]) {
				edgeOptions.add(LEFT);
				cellOptions.remove(LEFT);
			}
			
			if (edgeOptions.size() > 0) {
				edge = edgeOptions.get(random.nextInt(edgeOptions.size()));
				edges[edge][c.getX()][c.getY()] = false;
				// Lots of hax
				edges[(edge + 2) % 4][c.getX() + (int) Math.round(Math.sin(edge * Math.PI / 2))][c.getY() - (int) Math.round(Math.cos(edge * Math.PI / 2))] = false;
			}
			
			// Expand
			for (Integer direction : cellOptions) {
				// Lots of hax again
				Coordinate test = new SimpleCoordinate(c.getX() + (int) Math.round(Math.sin(direction * Math.PI / 2)), c.getY() - (int) Math.round(Math.cos(direction * Math.PI / 2)));
				if (!openSet.contains(test)) {
					openSet.add(test);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see Maze#wallUp(Coordinate)
	 */
	@Override
	public boolean wallUp(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return edges[TOP][c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallRight(Coordinate)
	 */
	@Override
	public boolean wallRight(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return edges[RIGHT][c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallDown(Coordinate)
	 */
	@Override
	public boolean wallDown(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return edges[BOT][c.getX()][c.getY()];
	}

	/* (non-Javadoc)
	 * @see Maze#wallLeft(Coordinate)
	 */
	@Override
	public boolean wallLeft(Coordinate c) throws IllegalArgumentException {
		if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		return edges[LEFT][c.getX()][c.getY()];
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
	
	public void drawMaze() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print("+");
				if (this.wallUp(new SimpleCoordinate(x, y))) {
					System.out.print("-");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("+");
			
			for (int x = 0; x < width; x++) {
				if (this.wallLeft(new SimpleCoordinate(x, y))) {
					System.out.print("|");
				} else {
					System.out.print(" ");
				}
				System.out.print(" ");
			}
			if (this.wallRight(new SimpleCoordinate(width - 1, y))) {
				System.out.println("|");
			} else {
				System.out.println(" ");
			}
		}
		
		for (int x = 0; x < width; x++) {
			System.out.print("+");
			if (this.wallDown(new SimpleCoordinate(x, height - 1))) {
				System.out.print("-");
			} else {
				System.out.print(" ");
			}
		}
		System.out.println("+");
		System.out.println();
	}
	
	int width;
	int height;
	boolean[][][] edges;
	public static final Integer TOP = 0;
	public static final Integer RIGHT = 1;
	public static final Integer BOT = 2;
	public static final Integer LEFT = 3;
}
