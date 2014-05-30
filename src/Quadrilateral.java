/**
 * A Quadrilateral Class based off Matrices
 * 
 * @author Gabriel
 */
public class Quadrilateral extends Matrix {
	/**
	 * Constructs a closed quadrilateral from (x1, y1) to (x2, y2) to (x3, y3)
	 * to (x4, y4)
	 * 
	 * @param x1
	 *            x ordinate of the first point
	 * @param y1
	 *            y ordinate of the first point
	 * @param x2
	 *            x ordinate of the second point
	 * @param y2
	 *            y ordinate of the second point
	 * @param x3
	 *            x ordinate of the third point
	 * @param y3
	 *            y ordinate of the third point
	 * @param x4
	 *            x ordinate of the fourth point
	 * @param y4
	 *            y ordinate of the fourth point
	 */
	public Quadrilateral(double x1, double y1, double x2, double y2, double x3,
			double y3, double x4, double y4) {
		super(2, 4);
		data[0][0] = x1;
		data[1][0] = y1;
		data[0][1] = x2;
		data[1][1] = y2;
		data[0][2] = x3;
		data[1][2] = y3;
		data[0][3] = x4;
		data[1][3] = y4;
	}

	/**
	 * Constructs a closed quadrilateral from p1 to p2 to p3 to p4
	 * 
	 * @param p1
	 *            The first point
	 * @param p2
	 *            The second point
	 * @param p3
	 *            The third point
	 * @param p4
	 *            The fourth point
	 */
	public Quadrilateral(Point p1, Point p2, Point p3, Point p4) {
		super(2, 4);
		data[0][0] = p1.getX();
		data[1][0] = p1.getY();
		data[0][1] = p2.getX();
		data[1][1] = p2.getY();
		data[0][2] = p3.getX();
		data[1][2] = p3.getY();
		data[0][3] = p4.getX();
		data[1][3] = p4.getY();
	}

	/**
	 * Constructs a copy of a quadrilateral
	 * 
	 * @param q
	 *            The quadrilateral
	 */
	public Quadrilateral(Quadrilateral q) {
		super(q);
	}

	/**
	 * Constructs a quadrilateral from a suitable matrix
	 * 
	 * @param m
	 *            The matrix
	 */
	public Quadrilateral(Matrix m) {
		super(m);
		if (m.getRows() != 2 || m.getColumns() != 4) {
			throw new IllegalArgumentException(
					"Matrix not of correct dimensions");
		}
	}

	/**
	 * Checks whether the quadrilateral intersects with a line
	 * 
	 * @param l
	 *            The line
	 * @return whether the quadrilateral intersects with the line
	 */
	public boolean intersectsLine(Line l) {
		if (l == null) {
			return false;
		}

		return (l.intersection(new Line(getX(1), getY(1), getX(2), getY(2))) != null
				|| l.intersection(new Line(getX(2), getY(2), getX(3), getY(3))) != null
				|| l.intersection(new Line(getX(3), getY(3), getX(4), getY(4))) != null || l
					.intersection(new Line(getX(4), getY(4), getX(1), getY(1))) != null);
	}

	@Override
	public String toString() {
		return getX(1) + " " + getY(1) + " to " + getX(2) + " " + getY(2)
				+ " to " + getX(3) + " " + getY(3) + " to " + getX(4) + " "
				+ getY(4);
	}

	/**
	 * Getter for the x ordinate of a point
	 * 
	 * @param point
	 *            The index of the point
	 * @return The x ordinate
	 */
	public double getX(int point) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}

		return data[0][point - 1];
	}

	/**
	 * Getter for the y ordinate of a point
	 * 
	 * @param point
	 *            The index of the point
	 * @return The y ordinate
	 */
	public double getY(int point) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}

		return data[1][point - 1];
	}

	/**
	 * Setter for the x ordinate of a point
	 * 
	 * @param point
	 *            The index of the point
	 * @param x
	 *            The new x value
	 */
	public void setX(int point, double x) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}

		data[0][point - 1] = x;
	}

	/**
	 * Setter for the y ordinate of a point
	 * 
	 * @param point
	 *            The index of the point
	 * @param y
	 *            The new y value
	 */
	public void setY(int point, double y) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}

		data[1][point - 1] = y;
	}
}
