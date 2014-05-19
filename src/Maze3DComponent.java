import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JComponent;

/**
 * A Maze Component
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
				if (e.getKeyCode() == KeyEvent.VK_UP && !maze.getWall(player, playerFacing)) {
					if (playerFacing == Maze.UP) {
						player.setY(player.getY() - 1);
					} else if (playerFacing == Maze.RIGHT) {
						player.setX(player.getX() + 1);
					} else if (playerFacing == Maze.DOWN) {
						player.setY(player.getY() + 1);
					} else if (playerFacing == Maze.LEFT) {
						player.setX(player.getX() - 1);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					playerFacing = (playerFacing + 1) % 4;
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !maze.getWall(player, (playerFacing + 2) % 4)) {
					if (playerFacing == Maze.UP) {
						player.setY(player.getY() + 1);
					} else if (playerFacing == Maze.RIGHT) {
						player.setX(player.getX() - 1);
					} else if (playerFacing == Maze.DOWN) {
						player.setY(player.getY() - 1);
					} else if (playerFacing == Maze.LEFT) {
						player.setX(player.getX() + 1);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
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
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int centreX = getWidth() / 2;
		int centreY = getHeight() / 2;
		Coordinate tempCoord = new SimpleCoordinate(player);
		int depth = 1;
		while (true) {
			if (maze.getWall(tempCoord, (playerFacing + 1) % 4)) {
				g2D.drawLine(centreX + (centreX / depth), centreY - (centreY / depth), (int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))));
				g2D.drawLine(centreX + (centreX / depth), centreY + (centreY / depth), (int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
			} else {
				g2D.drawLine(centreX + (centreX / depth), centreY - (centreY / depth), centreX + (centreX / depth), centreY + (centreY / depth));
				g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
				g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), centreX + (centreX / depth), (int) (centreY - (centreY / (depth + 0.5))));
				g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))), centreX + (centreX / depth), (int) (centreY + (centreY / (depth + 0.5))));
			}
			
			if (maze.getWall(tempCoord, (playerFacing + 3) % 4)) {
				g2D.drawLine(centreX - (centreX / depth), centreY - (centreY / depth), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))));
				g2D.drawLine(centreX - (centreX / depth), centreY + (centreY / depth), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
			} else {
				g2D.drawLine(centreX - (centreX / depth), centreY - (centreY / depth), centreX - (centreX / depth), centreY + (centreY / depth));
				g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
				g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), centreX - (centreX / depth), (int) (centreY - (centreY / (depth + 0.5))));
				g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))), centreX - (centreX / depth), (int) (centreY + (centreY / (depth + 0.5))));
			}
			
			if (maze.getWall(tempCoord, playerFacing)) {
				break;
			}
			
			g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX + (centreX / (depth + 1))), (int) (centreY - (centreY / (depth + 1))));
			g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))), (int) (centreX + (centreX / (depth + 1))), (int) (centreY + (centreY / (depth + 1))));
			g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 1))), (int) (centreY - (centreY / (depth + 1))));
			g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 1))), (int) (centreY + (centreY / (depth + 1))));
			
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
		
		g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))));
		g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
		g2D.drawLine((int) (centreX + (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
		g2D.drawLine((int) (centreX - (centreX / (depth + 0.5))), (int) (centreY - (centreY / (depth + 0.5))), (int) (centreX - (centreX / (depth + 0.5))), (int) (centreY + (centreY / (depth + 0.5))));
	}

	private Maze maze;
	private Coordinate player;
	private int playerFacing;
	private static final long serialVersionUID = 1L;
}
