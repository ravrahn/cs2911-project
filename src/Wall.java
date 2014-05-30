import java.awt.Color;

/**
 * @author Gabriel
 * 
 */
public class Wall {
	/**
	 * Constructs a wall at the base of given color
	 * 
	 * @param base
	 *            The base of the wall in euclidean space
	 * @param color
	 *            The color of the wall
	 */
	public Wall(Line base, Color color) {
		this.base = base;
		this.color = color;
	}

	@Override
	public String toString() {
		return base.toString() + " in " + color;
	}

	/**
	 * Getter for the base
	 * 
	 * @return the base
	 */
	public Line getBase() {
		return base;
	}

	/**
	 * Getter for the wall color
	 * 
	 * @return the wall color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for the base
	 * 
	 * @param base
	 *            The new base
	 */
	public void setBase(Line base) {
		this.base = base;
	}

	/**
	 * Setter for the color
	 * 
	 * @param color
	 *            The new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	private Line base;
	private Color color;
}
