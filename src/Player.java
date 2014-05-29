import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A Player
 * 
 * @author Gabriel
 */
public class Player {
	public Player() {
		position = new Point(0.5, 0.5);
		lastPosition = new Point(position);
		angle = Math.PI / 4;
		velocity = 0;
		width = 0.4;
		height = 0.4;
		xVelocity = 0;
		yVelocity = 0;
		double[][] identity = { { 1, 0 }, { 0, 1 } };
		scaleTransform = new Matrix(identity, identity.length,
				identity[0].length);
	}

	public void update(ArrayList<Wall> mazeWalls) {
		lastPosition = new Point(position);
		position.setX(position.getX() + velocity * Math.cos(angle));
		Quadrilateral xHitbox = genHitbox();
		position.setX(lastPosition.getX());
		position.setY(position.getY() + velocity * Math.sin(angle));
		Quadrilateral yHitbox = genHitbox();
		position.setX(position.getX() + velocity * Math.cos(angle));
		Quadrilateral hitbox = genHitbox();
		boolean xMove = true;
		boolean yMove = true;
		boolean move = true;
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

	public Quadrilateral genHitbox() {
		return new Quadrilateral(position.getX() - width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				- height / 2, position.getX() + width / 2, position.getY()
				+ height / 2, position.getX() - width / 2, position.getY()
				+ height / 2);
	}

	public void draw(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(Color.BLACK);
		Quadrilateral drawBox = new Quadrilateral(
				scaleTransform.multiply(genHitbox()));
		g2D.fillOval((int) drawBox.getX(1), (int) drawBox.getY(1),
				(int) (drawBox.getX(3) - drawBox.getX(1)),
				(int) (drawBox.getY(3) - drawBox.getY(1)));
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

	public void addVelocity(double vel) {
		velocity += vel;
	}

	public void addAngle(double angle) {
		this.angle += angle;
		int rotations = (int) (this.angle / (2 * Math.PI));
		this.angle -= 2 * Math.PI * rotations;
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
}
