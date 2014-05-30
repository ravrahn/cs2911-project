/**
 * A Point Class based of Matrices
 * 
 * @author Gabriel
 */
public class Point extends Matrix {
	/**
	 * Constructs a point at (x, y)
	 * 
	 * @param x
	 *            The x ordinate
	 * @param y
	 *            The y ordinate
	 */
	public Point(double x, double y) {
		super(2, 1);
		data[0][0] = x;
		data[1][0] = y;
	}

	/**
	 * Constructs a copy of a point
	 * 
	 * @param p
	 *            The point
	 */
	public Point(Point p) {
		super(2, 1);
		data[0][0] = p.getX();
		data[1][0] = p.getY();
	}

	/**
	 * Constructs a point from a suitable matrix
	 * 
	 * @param m
	 *            The matrix
	 */
	public Point(Matrix m) {
		super(m);
		if (m.getRows() != 2 || m.getColumns() != 1) {
			throw new IllegalArgumentException(
					"Matrix not of correct dimensions");
		}
	}

	/**
	 * Calculates the distance between this point and another point
	 * 
	 * @param p
	 *            The other point
	 * @return The distance
	 */
	public double distance(Point p) {
		if (p.equals(null)) {
			return 0;
		}
		double xDiff = getX() - p.getX();
		double yDiff = getY() - p.getY();
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	@Override
	public String toString() {
		return getX() + " " + getY();
	}

	/**
	 * Getter for x
	 * 
	 * @return x
	 */
	public double getX() {
		return data[0][0];
	}

	/**
	 * Getter for y
	 * 
	 * @return y
	 */
	public double getY() {
		return data[1][0];
	}

	/**
	 * Setter for x
	 * 
	 * @param x
	 *            x
	 */
	public void setX(double x) {
		data[0][0] = x;
	}

	/**
	 * Setter for y
	 * 
	 * @param y
	 *            y
	 */
	public void setY(double y) {
		data[1][0] = y;
	}
}
