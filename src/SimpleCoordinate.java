/**
 * Simple Coordinate Implementation Class
 * 
 * @author Gabriel
 */
public class SimpleCoordinate implements Coordinate {
	/**
	 * Constructs a simple coordinate at (x, y)
	 * 
	 * @param x
	 *            The x ordinate
	 * @param y
	 *            The y ordinate
	 */
	public SimpleCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructs a copy of a coordinate
	 * 
	 * @param c
	 *            The coordinate
	 */
	public SimpleCoordinate(Coordinate c) {
		this.x = c.getX();
		this.y = c.getY();
	}

	@Override
	public String toString() {
		return x + " " + y;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Coordinate) {
			Coordinate c = (Coordinate) o;
			return (x == c.getX() && y == c.getY());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Coordinate#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Coordinate#setX(int)
	 */
	@Override
	public void setX(int x) {
		this.x = x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Coordinate#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Coordinate#setY(int)
	 */
	@Override
	public void setY(int y) {
		this.y = y;
	}

	private int x;
	private int y;
}
