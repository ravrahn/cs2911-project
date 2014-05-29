import java.awt.Color;
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
				cellOptions.remove(UP);
			} else if (inMaze[c.getX()][c.getY() - 1]) {
				edgeOptions.add(UP);
				cellOptions.remove(UP);
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
				cellOptions.remove(DOWN);
			} else if (inMaze[c.getX()][c.getY() + 1]) {
				edgeOptions.add(DOWN);
				cellOptions.remove(DOWN);
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
	 * @see Maze#getWall(Coordinate, int)
	 */
	@Override
	public boolean getWall(Coordinate c, int direction) {
		if (c.getX() < 0 || c.getX() >= width || c.getY() < 0 || c.getY() >= height) {
			throw new IllegalArgumentException("Outside bounds of maze");
		}
		if (direction < 0 || direction > 3) {
			throw new IllegalArgumentException("Invalid direction");
		}
		return edges[direction][c.getX()][c.getY()];
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
				if (getWall(new SimpleCoordinate(x, y), UP)) {
					System.out.print("-");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("+");
			
			for (int x = 0; x < width; x++) {
				if (getWall(new SimpleCoordinate(x, y), LEFT)) {
					System.out.print("|");
				} else {
					System.out.print(" ");
				}
				System.out.print(" ");
			}
			if (getWall(new SimpleCoordinate(width - 1, y), RIGHT)) {
				System.out.println("|");
			} else {
				System.out.println(" ");
			}
		}
		
		for (int x = 0; x < width; x++) {
			System.out.print("+");
			if (getWall(new SimpleCoordinate(x, height - 1), DOWN)) {
				System.out.print("-");
			} else {
				System.out.print(" ");
			}
		}
		System.out.println("+");
		System.out.println();
	}
	
	public ArrayList<Wall> toWalls() {
		ArrayList<Wall> walls = new ArrayList<Wall>();
		Coordinate currCoord = new SimpleCoordinate(0, 0);
		Point lastPoint = new Point(0, 0);
		Point currPoint = new Point(0, 0);
		int length = 0;
		int currDirection = Maze.UP;
		Point endPoint = new Point(0, 1);
		while (!currPoint.equals(endPoint)) {
			// Save the current Point
			currPoint = new Point(lastPoint);
			if (currDirection == Maze.UP) {
				currPoint.setX(lastPoint.getX() + length);
			} else if (currDirection == Maze.RIGHT) {
				currPoint.setY(lastPoint.getY() + length);
			} else if (currDirection == Maze.DOWN) {
				currPoint.setX(lastPoint.getX() - length);
			} else if (currDirection == Maze.LEFT) {
				currPoint.setY(lastPoint.getY() - length);
			}
			
			// If no wall up (relative to currDirection)
			if (getWall(currCoord, currDirection)) {
				length++;
				
				// If wall to right
				if (getWall(currCoord, (currDirection + 1) % 4)) {
					Point newPoint = new Point(lastPoint);
					if (currDirection == Maze.UP) {
						newPoint.setX(newPoint.getX() + length);
					} else if (currDirection == Maze.RIGHT) {
						newPoint.setY(newPoint.getY() + length);
					} else if (currDirection == Maze.DOWN) {
						newPoint.setX(newPoint.getX() - length);
					} else if (currDirection == Maze.LEFT) {
						newPoint.setY(newPoint.getY() - length);
					}
					
					// Add line
					walls.add(new Wall(new Line(lastPoint, newPoint), directionToColor(currDirection)));
					// Reset points
					lastPoint = newPoint;
					length = 0;
					// Rotate right
					currDirection = (currDirection + 1) % 4;
				} else {
					// No wall to right
					length++;
					
					// Move right
					if (currDirection == Maze.UP) {
						currCoord.setX(currCoord.getX() + 1);
					} else if (currDirection == Maze.RIGHT) {
						currCoord.setY(currCoord.getY() + 1);
					} else if (currDirection == Maze.DOWN) {
						currCoord.setX(currCoord.getX() - 1);
					} else if (currDirection == Maze.LEFT) {
						currCoord.setY(currCoord.getY() - 1);
					}
				}
			} else {
				// No wall up
				// Add line
				walls.add(new Wall(new Line(lastPoint, currPoint), directionToColor(currDirection)));
				// Reset points
				lastPoint = currPoint;
				length = 1;
				
				// Move up
				if (currDirection == Maze.UP) {
					currCoord.setY(currCoord.getY() - 1);
				} else if (currDirection == Maze.RIGHT) {
					currCoord.setX(currCoord.getX() + 1);
				} else if (currDirection == Maze.DOWN) {
					currCoord.setY(currCoord.getY() + 1);
				} else if (currDirection == Maze.LEFT) {
					currCoord.setX(currCoord.getX() - 1);
				}
				// Rotate left
				currDirection = (currDirection + 3) % 4;
			}
		}
		if (walls.get(walls.size() - 1).getBase().getX1() == 0) {
			walls.get(walls.size() - 1).getBase().setY2(0);
		} else {
			walls.add(new Wall(new Line(0, 1, 0, 0), directionToColor(Maze.LEFT)));
		}
		
		return walls;
	}
	
	protected Color directionToColor(int direction) {
		if (direction < 0 || direction > 3) {
			throw new IllegalArgumentException("Invalid direction");
		}
		if (direction == Maze.UP) {
			return new Color(0xc2dcec);
		}
		if (direction == Maze.RIGHT) {
			return new Color(0x86b9d8);
		}
		if (direction == Maze.LEFT) {
			return new Color(0x659ec8);
		}
		if (direction == Maze.DOWN) {
			return new Color(0x3a84b5);
		}
		return Color.BLACK;
	}
	
	int width;
	int height;
	boolean[][][] edges;
}
