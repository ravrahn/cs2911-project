/**
 * A Point Class based of Matrices
 * 
 * @author Gabriel
 */
public class Point extends Matrix {
	public Point(double x, double y) {
		super(2, 1);
		data[0][0] = x;
		data[1][0] = y;
	}
	
	public Point(Point p) {
		super(2, 1);
		data[0][0] = p.getX();
		data[1][0] = p.getY();
	}
	
	public Point(Matrix m) {
		super(m);
		if (m.getRows() != 2 || m.getColumns() != 1) {
			throw new IllegalArgumentException("Matrix not of correct dimensions");
		}
	}
	
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
	
	public double getX() {
		return data[0][0];
	}
	
	public double getY() {
		return data[1][0];
	}
	
	public void setX(double x) {
		data[0][0] = x;
	}
	
	public void setY(double y) {
		data[1][0] = y;
	}
}
