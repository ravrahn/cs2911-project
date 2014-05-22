/**
 * @author Gabriel
 *
 */
public class Quadrilateral extends Matrix {
	public Quadrilateral(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
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
	
	public Quadrilateral(Quadrilateral q) {
		super(q);
	}
	
	public Quadrilateral(Matrix m) {
		super(m);
		if (m.getRows() != 2 || m.getColumns() != 4) {
			throw new IllegalArgumentException("Matrix not of correct dimensions");
		}
	}
	
	public boolean intersectsLine(Line l) {
		if (l == null) {
			return false;
		}
		
		return (l.intersection(new Line(getX(1), getY(1), getX(2), getY(2))) != null || l.intersection(new Line(getX(2), getY(2), getX(3), getY(3))) != null || l.intersection(new Line(getX(3), getY(3), getX(4), getY(4))) != null || l.intersection(new Line(getX(4), getY(4), getX(1), getY(1))) != null);
	}
	
	@Override
	public String toString() {
		return getX(1) + " " + getY(1) + " to " + getX(2) + " " + getY(2) + " to " + getX(3) + " " + getY(3) + " to " + getX(4) + " " + getY(4);
	}
	
	public double getX(int point) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}
		
		return data[0][point - 1];
	}
	
	public double getY(int point) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}
		
		return data[1][point - 1];
	}
	
	public void setX(int point, double x) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}
		
		data[0][point - 1] = x;
	}
	
	public void setY(int point, double y) {
		if (point < 1 || point > 4) {
			throw new IllegalArgumentException("Invalid point");
		}
		
		data[1][point - 1] = y;
	}
}
