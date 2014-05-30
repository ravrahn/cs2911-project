/**
 * A Maze Implementation Class with a Start and End Point
 * 
 * @author Gabriel
 */

// Unused
public class StartEndMaze implements Maze {
	public StartEndMaze(int width, int height, Coordinate start, Coordinate end) {
		backMaze = new SimpleMaze(width, height);

		if (start.getX() < 0 || start.getX() >= width || start.getY() < 0
				|| start.getY() >= height) {
			throw new IllegalArgumentException("Start is outside maze");
		}
		if (end.getX() < 0 || end.getX() >= width || end.getY() < 0
				|| end.getY() >= height) {
			throw new IllegalArgumentException("End is outside maze");
		}
		this.start = start;
		this.end = end;
	}
	
	@Override
	public boolean getWall(Coordinate c, int direction) {
		return backMaze.getWall(c, direction);
	}

	@Override
	public int getWidth() {
		return backMaze.getWidth();
	}

	@Override
	public int getHeight() {
		return backMaze.getHeight();
	}

	public Coordinate getStart() {
		return start;
	}

	public Coordinate getEnd() {
		return end;
	}

	public void drawMaze() {
		for (int y = getHeight() - 1; y >= 0; y--) {
			for (int x = 0; x < getWidth(); x++) {
				System.out.print("+");
				if (getWall(new SimpleCoordinate(x, y), UP)) {
					System.out.print("-");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("+");

			for (int x = 0; x < getWidth(); x++) {
				if (getWall(new SimpleCoordinate(x, y), LEFT)) {
					System.out.print("|");
				} else {
					System.out.print(" ");
				}
				Coordinate location = new SimpleCoordinate(x, y);
				if (start.equals(location)) {
					System.out.print("#");
				} else if (end.equals(location)) {
					System.out.print("@");
				} else {
					System.out.print(" ");
				}
			}
			if (getWall(new SimpleCoordinate(getWidth() - 1, y), RIGHT)) {
				System.out.println("|");
			} else {
				System.out.println(" ");
			}
		}

		for (int x = 0; x < getWidth(); x++) {
			System.out.print("+");
			if (getWall(new SimpleCoordinate(x, 0), DOWN)) {
				System.out.print("-");
			} else {
				System.out.print(" ");
			}
		}
		System.out.println("+");
		System.out.println();
	}

	Maze backMaze;
	Coordinate start;
	Coordinate end;
}
