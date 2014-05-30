/**
 * A Line Class based off Matrices
 * 
 * @author Gabriel
 */
public class Line extends Matrix {
	/**
	 * Constructs a line from (x1, y1) to (x2, y2)
	 * 
	 * @param x1
	 *            x ordinate of the first point
	 * @param y1
	 *            y ordinate of the first point
	 * @param x2
	 *            x ordinate of the second point
	 * @param y2
	 *            y ordinate of the second point
	 */
	public Line(double x1, double y1, double x2, double y2) {
		super(2, 2);
		data[0][0] = x1;
		data[1][0] = y1;
		data[0][1] = x2;
		data[1][1] = y2;
	}

	/**
	 * Constructs a line from p1 to p2
	 * 
	 * @param p1
	 *            The first point
	 * @param p2
	 *            The second point
	 */
	public Line(Point p1, Point p2) {
		super(2, 2);
		data[0][0] = p1.getX();
		data[1][0] = p1.getY();
		data[0][1] = p2.getX();
		data[1][1] = p2.getY();
	}

	/**
	 * Constructs a copy of a line
	 * 
	 * @param l
	 *            The line
	 */
	public Line(Line l) {
		super(l);
	}

	/**
	 * Constructs a line from a suitable matrix
	 * 
	 * @param m
	 *            The matrix
	 */
	public Line(Matrix m) {
		super(m);
		// Check if not of correct dimensions
		if (m.getRows() != 2 || m.getColumns() != 2) {
			throw new IllegalArgumentException(
					"Matrix not of correct dimensions");
		}
	}

	/**
	 * Returns the point of intersection of 2 lines if they intersect, otherwise
	 * returns null
	 * 
	 * @param l
	 *            The line to intersect with
	 * @return The point of intersection (or null)
	 */
	public Point intersection(Line l) {
		if (l == null) {
			return null;
		}

		double x1 = getX1();
		double y1 = getY1();
		double x2 = getX2();
		double y2 = getY2();
		double x3 = l.getX1();
		double y3 = l.getY1();
		double x4 = l.getX2();
		double y4 = l.getY2();

		double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

		// Parallel (give or take for precision errors)
		if (Math.abs(denom) < 0.0001) {
			return null;
		}

		double xNum = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
				* (x3 * y4 - y3 * x4);
		double yNum = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
				* (x3 * y4 - y3 * x4);
		Point p = new Point(xNum / denom, yNum / denom);

		return (inBounds(p) && l.inBounds(p)) ? p : null;
	}

	/**
	 * Returns whether the point is inside the bounds of the line
	 * 
	 * @param p
	 *            The point
	 * @return whether the point is inside the bounds of the line
	 */
	public boolean inBounds(Point p) {
		double xMin = Math.min(getX1(), getX2()) - 0.0001;
		double xMax = Math.max(getX1(), getX2()) + 0.0001;
		double yMin = Math.min(getY1(), getY2()) - 0.0001;
		double yMax = Math.max(getY1(), getY2()) + 0.0001;
		return (p.getX() >= xMin && p.getX() <= xMax && p.getY() >= yMin && p
				.getY() <= yMax);
	}

	@Override
	public String toString() {
		return getX1() + " " + getY1() + " to " + getX2() + " " + getY2();
	}

	/**
	 * Getter for x1
	 * 
	 * @return x1
	 */
	public double getX1() {
		return data[0][0];
	}

	/**
	 * Getter for y1
	 * 
	 * @return y1
	 */
	public double getY1() {
		return data[1][0];
	}

	/**
	 * Getter for x2
	 * 
	 * @return x2
	 */
	public double getX2() {
		return data[0][1];
	}

	/**
	 * Getter for y2
	 * 
	 * @return y2
	 */
	public double getY2() {
		return data[1][1];
	}

	/**
	 * Setter for x1
	 * 
	 * @param x
	 *            x1
	 */
	public void setX1(double x) {
		data[0][0] = x;
	}

	/**
	 * Setter for y1
	 * 
	 * @param y
	 *            y1
	 */
	public void setY1(double y) {
		data[1][0] = y;
	}

	/**
	 * Setter for x2
	 * 
	 * @param x
	 *            x2
	 */
	public void setX2(double x) {
		data[0][1] = x;
	}

	/**
	 * Setter for y2
	 * 
	 * @param y
	 *            y2
	 */
	public void setY2(double y) {
		data[1][1] = y;
	}
}
