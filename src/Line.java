
/**
 * A Line Class based off Matrices
 * @author Gabriel
 */
public class Line extends Matrix {
	public Line(double x1, double y1, double x2, double y2) {
		super(2, 2);
		data[0][0] = x1;
		data[1][0] = y1;
		data[0][1] = x2;
		data[1][1] = y2;
	}
	
	public Line(Point p1, Point p2) {	
		super(2, 2);
		data[0][0] = p1.getX();
		data[1][0] = p1.getY();
		data[0][1] = p2.getX();
		data[1][1] = p2.getY();
	}
	
	public Line(Line l) {
		super(l);
	}
	
	public Line(Matrix m) {
		super(m);
		if (m.getRows() != 2 || m.getColumns() != 2) {
			throw new IllegalArgumentException("Matrix not of correct dimensions");
		}
	}
	
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
		
		double denom = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
		
		// Parallel (give or take for precision errors)
		if (Math.abs(denom) < 0.0001) {
			return null;
		}
		
		double xNum = (x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4);
		double yNum = (x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4);
		Point p = new Point(xNum/denom, yNum/denom);

		return (inBounds(p) && l.inBounds(p)) ? p : null;
	}
	
	public boolean inBounds(Point p) {
		double xMin = Math.min(getX1(), getX2()) - 0.0001;
		double xMax = Math.max(getX1(), getX2()) + 0.0001;
		double yMin = Math.min(getY1(), getY2()) - 0.0001;
		double yMax = Math.max(getY1(), getY2()) + 0.0001;
		return (p.getX() >= xMin && p.getX() <= xMax && p.getY() >= yMin && p.getY() <= yMax);
	}
	
	@Override
	public String toString() {
		return getX1() + " " + getY1() + " to " + getX2() + " " + getY2();
	}
	
	public double getX1() {
		return data[0][0];
	}
	
	public double getY1() {
		return data[1][0];
	}
	
	public double getX2() {
		return data[0][1];
	}
	
	public double getY2() {
		return data[1][1];
	}
	
	public void setX1(double x) {
		data[0][0] = x;
	}
	
	public void setY1(double y) {
		data[1][0] = y;
	}
	
	public void setX2(double x) {
		data[0][1] = x;
	}
	
	public void setY2(double y) {
		data[1][1] = y;
	}
}
