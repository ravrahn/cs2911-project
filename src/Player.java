import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * A Player
 * 
 * @author Gabriel
 */
public class Player {
	/**
	 * Constructs a new player
	 */
	public Player() {
		position = new Point(0.5, 0.5);
		lastPosition = new Point(position);
		angle = Math.PI / 4;
		velocity = 0;
		width = 0.4;
		height = 0.4;
		double[][] identity = { { 1, 0 }, { 0, 1 } };
		scaleTransform = new Matrix(identity, identity.length,
				identity[0].length);
	}

	/**
	 * Updates the player's position, considering collisions with walls
	 * 
	 * @param mazeWalls
	 *            The walls to be considered
	 */
	public void update(ArrayList<Wall> mazeWalls) {
		// Save last position
		lastPosition = new Point(position);
		// Generate new positions
		position.setX(position.getX() + velocity * Math.cos(angle));
		Quadrilateral xHitbox = genHitbox();
		position.setX(lastPosition.getX());
		position.setY(position.getY() + velocity * Math.sin(angle));
		Quadrilateral yHitbox = genHitbox();
		position.setX(position.getX() + velocity * Math.cos(angle));
		Quadrilateral hitbox = genHitbox();

		// Setup flags
		boolean xMove = true;
		boolean yMove = true;
		boolean move = true;

		// Check for collision
		for (Wall wall : mazeWalls) {
			if (hitbox.intersectsLine(wall.getBase())) {
				move = false;
			}
			if (xHitbox.intersectsLine(wall.getBase())) {
				xMove = false;
			}
			if (yHitbox.intersectsLine(wall.getBase())) {
				yMove = false;
			}
		}

		// Try moving
		if (!move) {
			if (xMove) {
				position.setY(lastPosition.getY());
			} else if (yMove) {
				position.setX(lastPosition.getX());
			} else {
				position = lastPosition;
			}
		}
	}

	/**
	 * Generates a hitbox for the player
	 * 
	 * @return the hitbox
	 */
	public Quadrilateral genHitbox() {
		return new Quadrilateral(position.getX() - width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				+ height / 2, position.getX() - width / 2, position.getY()
				+ height / 2);
	}

	/**
	 * Draws the player
	 * 
	 * @param g
	 *            The graphics to draw on
	 */
	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(Color.BLACK);
		Quadrilateral drawBox = new Quadrilateral(
				scaleTransform.multiply(genHitbox()));
		g2D.fillOval((int) drawBox.getX(1), (int) drawBox.getY(1),
				(int) (drawBox.getX(3) - drawBox.getX(1)),
				(int) (drawBox.getY(3) - drawBox.getY(1)));
	}

	/**
	 * Getter for the player's position
	 * 
	 * @return the player's position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Getter for the player's orientation angle
	 * 
	 * @return the player's orientation angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Setter for the scaling transformation on the player for drawing
	 * 
	 * @param m
	 *            The scaling transformation matrix
	 */
	public void setScaleTransform(Matrix m) {
		scaleTransform = new Matrix(m);
	}

	/**
	 * Setter for the velocity
	 * 
	 * @param vel
	 *            The new velocity
	 */
	public void setVelocity(double vel) {
		velocity = vel;
	}

	/**
	 * Setter for the angle
	 * 
	 * @param angle
	 *            The new angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
		int rotations = (int) (this.angle / (2 * Math.PI));
		this.angle -= 2 * Math.PI * rotations;
	}

	/**
	 * Adds given velocity to the current velocity
	 * 
	 * @param vel
	 *            The velocity to add
	 */
	public void addVelocity(double vel) {
		velocity += vel;
	}

	/**
	 * Adds a given angle to the current angle
	 * 
	 * @param angle
	 *            The angle to add
	 */
	public void addAngle(double angle) {
		this.angle += angle;
		int rotations = (int) (this.angle / (2 * Math.PI));
		this.angle -= 2 * Math.PI * rotations;
	}

	// Player data
	Point position;
	Point lastPosition;
	// In radians
	double angle;
	double velocity;
	double width, height;
	Matrix scaleTransform;
}
