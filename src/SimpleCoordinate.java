/**
 * Simple Coordinate Implementation Class
 * 
 * @author Gabriel
 */
public class SimpleCoordinate implements Coordinate {
	/* (non-Javadoc)
	 * @see Coordinate#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/* (non-Javadoc)
	 * @see Coordinate#setX(int)
	 */
	@Override
	public void setX(int x) {
		this.x = x;
	}

	/* (non-Javadoc)
	 * @see Coordinate#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	/* (non-Javadoc)
	 * @see Coordinate#setY(int)
	 */
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	private int x;
	private int y;
}
