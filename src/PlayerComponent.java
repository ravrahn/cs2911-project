import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * A Player
 * 
 * @author Gabriel
 */
public class PlayerComponent extends JComponent {
	public PlayerComponent() {
		position = new Point(0.5, 0.5);
		lastPosition = new Point(position);
		angle = Math.PI / 2;
		velocity = 0;
		width = 0.4;
		height = 0.4;
		xVelocity = 0;
		yVelocity = 0;
		double[][] identity = { { 1, 0 }, { 0, 1 } };
		scaleTransform = new Matrix(identity, identity.length,
				identity[0].length);
		try {
			image = ImageIO.read(new File("fancy_stickman.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(ArrayList<Wall> mazeWalls) {
		lastPosition = new Point(position);
		position.setX(position.getX() + velocity * Math.cos(angle));
		position.setY(position.getY() + velocity * Math.sin(angle));

		Quadrilateral hitbox = genHitbox();
		boolean move = true;
		for (Wall wall : mazeWalls) {
			if (hitbox.intersectsLine(wall.getBase())) {
				move = false;
				break;
			}
		}

		if (!move) {
			position = lastPosition;
		}
	}

	public Quadrilateral genHitbox() {
		return new Quadrilateral(position.getX() - width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				+ height / 2, position.getX() - width / 2, position.getY()
				+ height / 2);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;

		Quadrilateral drawBox = new Quadrilateral(
				scaleTransform.multiply(genHitbox()));
		g2D.drawImage(image, (int) drawBox.getX(1), (int) drawBox.getY(1),
				(int) (drawBox.getX(3) - drawBox.getX(1)),
				(int) (drawBox.getY(3) - drawBox.getY(1)), null);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setScaleTransform(Matrix m) {
		scaleTransform = new Matrix(m);
	}

	public void setVelocity(double vel) {
		velocity = vel;
	}

	public void setAngle(double angle) {
		this.angle = angle;
		int rotations = (int) (this.angle / (2 * Math.PI));
		this.angle -= 2 * Math.PI * rotations;
	}

	public void setXVelocity(double xVel) {
		xVelocity = xVel;
	}

	public void setYVelocity(double yVel) {
		yVelocity = yVel;
	}

	public void addVelocity(double vel) {
		velocity += vel;
	}

	public void addAngle(double angle) {
		this.angle += angle;
		int rotations = (int) (this.angle / (2 * Math.PI));
		this.angle -= 2 * Math.PI * rotations;
	}

	public void addXVelocity(double xVel) {
		xVelocity += xVel;
	}

	public void addYVelocity(double yVel) {
		yVelocity += yVel;
	}

	// Centre of the x and y
	Point position;
	Point lastPosition;
	// In radians
	double angle;
	double velocity;
	double width, height;
	double xVelocity, yVelocity;
	Matrix scaleTransform;
	Image image;
	private static final long serialVersionUID = 1L;
}
