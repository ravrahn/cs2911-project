import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * A Maze Component
 * 
 * @author Gabriel
 */
public class MazeComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	public MazeComponent() throws IOException {
		maze = new SimpleMaze(10, 10);
		player = new SimpleCoordinate(0, 0);
		wallImage = ImageIO.read(new File("google_plus.png"));
		floorImage = ImageIO.read(new File("youtube.png"));
		playerImage = ImageIO.read(new File("facebook.png"));
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP && !maze.wallUp(player)) {
					player.setY(player.getY() - 1);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !maze.wallRight(player)) {
					player.setX(player.getX() + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !maze.wallDown(player)) {
					player.setY(player.getY() + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !maze.wallLeft(player)) {
					player.setX(player.getX() - 1);
				}
				
				repaint();
			}
		});
		
		setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
//		System.out.println(maze.getWidth() + " " + maze.getHeight());
//		System.out.println(getWidth() + " " + getHeight());
		int mazeWidth = maze.getWidth();
		int mazeHeight = maze.getHeight();
		int imgWidth = getWidth() / (2 * mazeWidth + 1);
		int imgHeight = getHeight() / (2 * mazeHeight + 1);
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				if (maze.wallUp(new SimpleCoordinate(x, y))) {
					g2D.drawImage(wallImage, (2 * x + 1) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				} else {
					g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
				}
			}
			g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * y) * imgHeight, imgWidth, imgHeight, null);
			
			for (int x = 0; x < mazeWidth; x++) {
				if (maze.wallLeft(new SimpleCoordinate(x, y))) {
					g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				} else {
					g2D.drawImage(floorImage, (2 * x) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				}
				if (player.equals(new SimpleCoordinate(x, y))) {
					g2D.drawImage(playerImage, (2 * x + 1) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				} else {
					g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
				}
			}
			if (maze.wallRight(new SimpleCoordinate(mazeWidth - 1, y))) {
				g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
			} else {
				g2D.drawImage(floorImage, (2 * mazeWidth) * imgWidth, (2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
			}
		}
		
		for (int x = 0; x < mazeWidth; x++) {
			g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
			if (maze.wallDown(new SimpleCoordinate(x, mazeHeight - 1))) {
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
}
