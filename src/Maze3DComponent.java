import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.JComponent;

/**
 * A Legacy 3D Maze Component
 * 
 * @author Gabriel
 */
public class Maze3DComponent extends JComponent {
	public Maze3DComponent() throws IOException {
		maze = new SimpleMaze(10, 10);
		player = new SimpleCoordinate(0, 0);
		playerFacing = Maze.UP;

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
						&& !maze.getWall(player, playerFacing)) {
					if (playerFacing == Maze.UP) {
						player.setY(player.getY() - 1);
					} else if (playerFacing == Maze.RIGHT) {
						player.setX(player.getX() + 1);
					} else if (playerFacing == Maze.DOWN) {
						player.setY(player.getY() + 1);
					} else if (playerFacing == Maze.LEFT) {
						player.setX(player.getX() - 1);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT
						|| e.getKeyCode() == KeyEvent.VK_D) {
					playerFacing = (playerFacing + 1) % 4;
				} else if ((e.getKeyCode() == KeyEvent.VK_DOWN || e
						.getKeyCode() == KeyEvent.VK_S)
						&& !maze.getWall(player, (playerFacing + 2) % 4)) {
					if (playerFacing == Maze.UP) {
						player.setY(player.getY() + 1);
					} else if (playerFacing == Maze.RIGHT) {
						player.setX(player.getX() - 1);
					} else if (playerFacing == Maze.DOWN) {
						player.setY(player.getY() - 1);
					} else if (playerFacing == Maze.LEFT) {
						player.setX(player.getX() + 1);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT
						|| e.getKeyCode() == KeyEvent.VK_A) {
					playerFacing = (playerFacing + 3) % 4;
				}

				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				grabFocus();
			}
		});

		setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		int centreX = getWidth() / 2;
		int centreY = getHeight() / 2;

		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform initTransform = g2D.getTransform();
		AffineTransform centredTransform = new AffineTransform();
		g2D.setTransform(centredTransform);
		g2D.translate(centreX, centreY);

		Color frontColor;
		Color rightColor;
		Color leftColor;
		if (playerFacing == Maze.UP) {
			frontColor = Color.BLUE;
			rightColor = Color.GREEN;
			leftColor = Color.RED;
		} else if (playerFacing == Maze.RIGHT) {
			frontColor = Color.GREEN;
			rightColor = Color.YELLOW;
			leftColor = Color.BLUE;
		} else if (playerFacing == Maze.DOWN) {
			frontColor = Color.YELLOW;
			rightColor = Color.RED;
			leftColor = Color.GREEN;
		} else {
			frontColor = Color.RED;
			rightColor = Color.BLUE;
			leftColor = Color.YELLOW;
		}

		Coordinate tempCoord = new SimpleCoordinate(player);
		int depth = 1;
		while (true) {
			if (maze.getWall(tempCoord, (playerFacing + 1) % 4)) {
				int[] polygonXs = { (centreX / depth),
						(int) ((centreX / (depth + 0.5))),
						(int) ((centreX / (depth + 0.5))), (centreX / depth) };
				int[] polygonYs = { -(centreY / depth),
						(int) (-(centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))), (centreY / depth) };
				Shape s = new Polygon(polygonXs, polygonYs, polygonXs.length);
				g2D.setColor(rightColor);
				g2D.fill(s);
				g2D.setColor(Color.BLACK);
			} else {
				int[] polygonXs = { (centreX / depth),
						(int) ((centreX / (depth + 0.5))),
						(int) ((centreX / (depth + 0.5))), (centreX / depth) };
				int[] polygonYs = { (int) (-(centreY / (depth + 0.5))),
						(int) (-(centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))) };
				Shape s = new Polygon(polygonXs, polygonYs, polygonXs.length);
				g2D.setColor(frontColor);
				g2D.fill(s);
				g2D.setColor(Color.BLACK);
			}

			if (maze.getWall(tempCoord, (playerFacing + 3) % 4)) {
				int[] polygonXs = { -(centreX / depth),
						(int) (-(centreX / (depth + 0.5))),
						(int) (-(centreX / (depth + 0.5))), -(centreX / depth) };
				int[] polygonYs = { -(centreY / depth),
						(int) (-(centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))), (centreY / depth) };
				Shape s = new Polygon(polygonXs, polygonYs, polygonXs.length);
				g2D.setColor(leftColor);
				g2D.fill(s);
				g2D.setColor(Color.BLACK);
			} else {
				int[] polygonXs = { -(centreX / depth),
						(int) (-(centreX / (depth + 0.5))),
						(int) (-(centreX / (depth + 0.5))), -(centreX / depth) };
				int[] polygonYs = { (int) (-(centreY / (depth + 0.5))),
						(int) (-(centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))),
						(int) ((centreY / (depth + 0.5))) };
				Shape s = new Polygon(polygonXs, polygonYs, polygonXs.length);
				g2D.setColor(frontColor);
				g2D.fill(s);
				g2D.setColor(Color.BLACK);
			}

			if (maze.getWall(tempCoord, playerFacing)) {
				break;
			}

			int[] polygonXs1 = { (int) ((centreX / (depth + 0.5))),
					(int) ((centreX / (depth + 1))),
					(int) ((centreX / (depth + 1))),
					(int) ((centreX / (depth + 0.5))) };
			int[] polygonYs1 = { (int) (-(centreY / (depth + 0.5))),
					(int) (-(centreY / (depth + 1))),
					(int) ((centreY / (depth + 1))),
					(int) ((centreY / (depth + 0.5))) };
			Shape s1 = new Polygon(polygonXs1, polygonYs1, polygonXs1.length);
			int[] polygonXs2 = { (int) (-(centreX / (depth + 0.5))),
					(int) (-(centreX / (depth + 1))),
					(int) (-(centreX / (depth + 1))),
					(int) (-(centreX / (depth + 0.5))) };
			int[] polygonYs2 = { (int) (-(centreY / (depth + 0.5))),
					(int) (-(centreY / (depth + 1))),
					(int) ((centreY / (depth + 1))),
					(int) ((centreY / (depth + 0.5))) };
			Shape s2 = new Polygon(polygonXs2, polygonYs2, polygonXs2.length);
			g2D.setColor(rightColor);
			g2D.fill(s1);
			g2D.setColor(leftColor);
			g2D.fill(s2);
			g2D.setColor(Color.BLACK);

			depth++;
			if (playerFacing == Maze.UP) {
				tempCoord.setY(tempCoord.getY() - 1);
			} else if (playerFacing == Maze.RIGHT) {
				tempCoord.setX(tempCoord.getX() + 1);
			} else if (playerFacing == Maze.DOWN) {
				tempCoord.setY(tempCoord.getY() + 1);
			} else if (playerFacing == Maze.LEFT) {
				tempCoord.setX(tempCoord.getX() - 1);
			}
		}

		int[] polygonXs = { (int) ((centreX / (depth + 0.5))),
				(int) (-(centreX / (depth + 0.5))),
				(int) (-(centreX / (depth + 0.5))),
				(int) ((centreX / (depth + 0.5))) };
		int[] polygonYs = { (int) (-(centreY / (depth + 0.5))),
				(int) (-(centreY / (depth + 0.5))),
				(int) ((centreY / (depth + 0.5))),
				(int) ((centreY / (depth + 0.5))) };
		Shape s = new Polygon(polygonXs, polygonYs, polygonXs.length);
		g2D.setColor(frontColor);
		g2D.fill(s);
		g2D.setColor(Color.BLACK);
		g2D.setTransform(initTransform);
	}

	private Maze maze;
	private Coordinate player;
	private int playerFacing;
	private static final long serialVersionUID = 1L;
}
