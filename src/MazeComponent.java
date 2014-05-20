import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * A Maze Component
 * 
 * @author Gabriel
 */
public class MazeComponent extends JComponent {
	public MazeComponent() throws IOException {
		maze = new SimpleMaze(20, 20);
		player = new SimpleCoordinate(0, 0);
		wallImage = ImageIO.read(new File("clear.png"));
		floorImage = ImageIO.read(new File("pantone.png"));
		playerImage = ImageIO.read(new File("player.png"));
		hintImage = ImageIO.read(new File("red.png"));
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP && !maze.getWall(player, Maze.UP)) {
					player.setY(player.getY() - 1);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !maze.getWall(player, Maze.RIGHT)) {
					player.setX(player.getX() + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !maze.getWall(player, Maze.DOWN)) {
					player.setY(player.getY() + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !maze.getWall(player, Maze.LEFT)) {
					player.setX(player.getX() - 1);
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
		
		
		System.out.println(maze.getWidth() + " " + maze.getHeight());
		System.out.println(getWidth() + " " + getHeight());
		int mazeWidth = maze.getWidth();
		int mazeHeight = maze.getHeight();
		int imgWidth = getWidth() / (2 * mazeWidth + 1);
		int imgHeight = getHeight() / (2 * mazeHeight + 1);
		
		List<SimpleCoordinate> hintPath = Hinter.getPath(maze, player, new SimpleCoordinate(mazeWidth-1, mazeHeight-1));
		
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				if (maze.getWall(new SimpleCoordinate(x, y), Maze.UP)) {
					g2D.drawImage(wallImage, (2 * x + 1) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				} else {
					g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				}
			}
			g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
			
			for (int x = 0; x < mazeWidth; x++) {
				if (maze.getWall(new SimpleCoordinate(x, y), Maze.LEFT)) {
					g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				} else {
					g2D.drawImage(floorImage, (2 * x) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				}
				g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				
				// add the player
				if (player.equals(new SimpleCoordinate(x, y))) {
					g2D.drawImage(playerImage, (2 * x + 1) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				}
			}
			
			if (maze.getWall(new SimpleCoordinate(mazeWidth - 1, y), Maze.RIGHT)) {
				g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
			} else {
				g2D.drawImage(floorImage, (2 * mazeWidth) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
			}
		}
		
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				// draw the hint path
				SimpleCoordinate here = new SimpleCoordinate(x, y);
				if (hintPath.contains(here)) {
					if (!player.equals(new SimpleCoordinate(x, y))) {
						// put the hint tile down
						g2D.drawImage(hintImage, (2 * x + 1) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
					}
					if (!(hintPath.indexOf(here) == hintPath.size()-1)) {
						if (hintPath.get(hintPath.indexOf(here) + 1).getX() > x) {
							// the next one is to the left
							g2D.drawImage(hintImage, (2 * x + 2) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
						} else if (hintPath.get(hintPath.indexOf(here) + 1).getX() < x) {
							// the next one is to the right
							g2D.drawImage(hintImage, (2 * x) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
						} else if (hintPath.get(hintPath.indexOf(here) + 1).getY() > y) {
							// the next one is below
							g2D.drawImage(hintImage, (2 * x + 1) * imgWidth, (2 * y + 2) * imgHeight, imgWidth, imgHeight, null);
						} else if (hintPath.get(hintPath.indexOf(here) + 1).getY() < y) {
							// the next one is above
							g2D.drawImage(hintImage, (2 * x + 1) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
						}
					}
				}
			}
		}
		
		
		for (int x = 0; x < mazeWidth; x++) {
			g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
			if (maze.getWall(new SimpleCoordinate(x, mazeHeight - 1), Maze.DOWN)) {
				g2D.drawImage(wallImage, (2 * x + 1) * imgWidth, (2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
			} else {
				g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
			}
		}
		g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
	}

	private Maze maze;
	private Coordinate player;
	private Image wallImage;
	private Image floorImage;
	private Image playerImage;
	private Image hintImage;
	private static final long serialVersionUID = 1L;
}
