import java.awt.Color;

/**
 * @author Gabriel
 *
 */
public class Wall {
	public Wall(Line base, Color color) {
		this.base = base;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return base.toString() + " in " + color;
	}
	
	public Line getBase() {
		return base;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setBase(Line base) {
		this.base = base;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	private Line base;
	private Color color;
}
