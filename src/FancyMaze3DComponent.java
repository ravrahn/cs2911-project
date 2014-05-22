import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author Gabriel
 * 
 */
public class FancyMaze3DComponent extends JComponent {
	public FancyMaze3DComponent() throws IOException {
		maze = new SimpleMaze(10, 10);
		mazeWalls = maze.toWalls();
		player = new PlayerComponent();
		wallImage = ImageIO.read(new File("water.png"));
		floorImage = ImageIO.read(new File("island_desert.png"));
		playerImage = ImageIO.read(new File("fancy_stickman.png"));

		keyboard = new KeyboardInput();
		addKeyListener(keyboard);

		running = true;
		paused = false;
		
		setFocusable(true);

		fps = 60;
		
		runGameLoop();
	}

	private void runGameLoop() {
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};

		loop.start();
	}

	private void gameLoop() {
		final double GAME_HERTZ = 30.0;
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final int MAX_UPDATES_BEFORE_RENDER = 3;
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		final double TARGET_FPS = 60.0;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;
			if (!paused) {
				// Do as many game updates as we need to, potentially playing
				// catchup.
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
					updateGame();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				// If for some reason an update takes forever, we don't want to
				// do an insane number of catchups.
				// If you were doing some sort of game that needed to keep EXACT
				// time, you would get rid of this.
				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				// Render. To do so, we need to calculate interpolation for a
				// smooth render.
				interpolation = Math.min(1.0f,
						((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
				repaint();
				lastRenderTime = now;

				// Update the frames we got.
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					System.out.println("NEW SECOND " + thisSecond + " "
							+ frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				// Yield until it has been at least the target time between
				// renders. This saves the CPU from hogging.
				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
						&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();

					// This stops the app from consuming all your CPU. It makes
					// this slightly less accurate, but is worth it.
					// You can remove this line and it will still work (better),
					// your CPU just climbs on certain OSes.
					// FYI on some OS's this can cause pretty bad stuttering.
					// Scroll down and have a look at different peoples'
					// solutions to this.
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}

					now = System.nanoTime();
				}
			}
		}
	}

	private void updateGame() {
		keyboard.poll();
		player.setVelocity(0);
		processInput();
		player.update(mazeWalls);
	}

	private void processInput() {
		if (keyboard.keyDown(KeyEvent.VK_UP) || keyboard.keyDown(KeyEvent.VK_W)) {
			player.addVelocity(0.05);
		}

		if (keyboard.keyDown(KeyEvent.VK_RIGHT) || keyboard.keyDown(KeyEvent.VK_D)) {
			player.addAngle(-Math.PI / 30);
		}

		if (keyboard.keyDown(KeyEvent.VK_DOWN) || keyboard.keyDown(KeyEvent.VK_S)) {
			player.addVelocity(-0.05);
		}

		if (keyboard.keyDown(KeyEvent.VK_LEFT) || keyboard.keyDown(KeyEvent.VK_A)) {
			player.addAngle(Math.PI / 30);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int mazeWidth = maze.getWidth();
		int mazeHeight = maze.getHeight();
		int imgWidth = getWidth() / (2 * mazeWidth + 1);
		int imgHeight = getHeight() / (2 * mazeHeight + 1);
//		for (int y = 0; y < mazeHeight; y++) {
//			for (int x = 0; x < mazeWidth; x++) {
//				g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y)
//						* imgHeight, imgWidth, imgHeight, null);
//				if (maze.getWall(new SimpleCoordinate(x, y), Maze.UP)) {
//					g2D.drawImage(wallImage, (2 * x + 1) * imgWidth, (2 * y)
//							* imgHeight, imgWidth, imgHeight, null);
//				} else {
//					g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y)
//							* imgHeight, imgWidth, imgHeight, null);
//				}
//			}
//			g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * y)
//					* imgHeight, imgWidth, imgHeight, null);
//
//			for (int x = 0; x < mazeWidth; x++) {
//				if (maze.getWall(new SimpleCoordinate(x, y), Maze.LEFT)) {
//					g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * y + 1)
//							* imgHeight, imgWidth, imgHeight, null);
//				} else {
//					g2D.drawImage(floorImage, (2 * x) * imgWidth, (2 * y + 1)
//							* imgHeight, imgWidth, imgHeight, null);
//				}
//				g2D.drawImage(floorImage, (2 * x + 1) * imgWidth, (2 * y + 1)
//						* imgHeight, imgWidth, imgHeight, null);
//				if (player.equals(new SimpleCoordinate(x, y))) {
//					g2D.drawImage(playerImage, (2 * x + 1) * imgWidth,
//							(2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
//				}
//			}
//			if (maze.getWall(new SimpleCoordinate(mazeWidth - 1, y), Maze.RIGHT)) {
//				g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth,
//						(2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
//			} else {
//				g2D.drawImage(floorImage, (2 * mazeWidth) * imgWidth,
//						(2 * y + 1) * imgHeight, imgWidth, imgHeight, null);
//			}
//		}
//
//		for (int x = 0; x < mazeWidth; x++) {
//			g2D.drawImage(wallImage, (2 * x) * imgWidth, (2 * mazeHeight)
//					* imgHeight, imgWidth, imgHeight, null);
//			if (maze.getWall(new SimpleCoordinate(x, mazeHeight - 1), Maze.DOWN)) {
//				g2D.drawImage(wallImage, (2 * x + 1) * imgWidth,
//						(2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
//			} else {
//				g2D.drawImage(floorImage, (2 * x + 1) * imgWidth,
//						(2 * mazeHeight) * imgHeight, imgWidth, imgHeight, null);
//			}
//		}
//		g2D.drawImage(wallImage, (2 * mazeWidth) * imgWidth, (2 * mazeHeight)
//				* imgHeight, imgWidth, imgHeight, null);
		
		g2D.translate(imgWidth, imgHeight);
		double[][] scaleArray = {{imgWidth, 0}, {0, imgHeight}};
		Matrix scaleTransform = new Matrix(scaleArray, scaleArray.length, scaleArray[0].length);
		ArrayList<Wall> scaledWalls = new ArrayList<Wall>();
		for (Wall wall : mazeWalls) {
			scaledWalls.add(new Wall(new Line(scaleTransform.multiply(wall.getBase())), wall.getColor()));
		}
		for (Wall wall : scaledWalls) {
			g2D.setColor(wall.getColor());
			g2D.drawLine((int) wall.getBase().getX1(), (int) wall.getBase().getY1(), (int) wall.getBase().getX2(), (int) wall.getBase().getY2());
		}
		
		player.setScaleTransform(scaleTransform);
		player.paintComponent(g2D);
		
		g2D.setColor(Color.BLACK);
		g2D.translate(-imgWidth, -imgHeight);
		
		for (int i = 0; i < getWidth(); i++) {
			double rayAngle = player.getAngle() - ((i - (getWidth() / 2)) * (Math.PI / 2000));
			Line ray = new Line(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getX() + 1000 * Math.cos(rayAngle), player.getPosition().getY() + 1000 * Math.sin(rayAngle));
			double minLength = Double.POSITIVE_INFINITY;
			int height = 300;
			Color color = Color.BLACK;
			for (Wall wall : mazeWalls) {
				Point p = ray.intersection(wall.getBase());
				if (p != null) {
					double dist = p.distance(player.getPosition());
					if (dist < minLength) {
						minLength = dist;
						if (dist == 0) dist = 0.1;
						height = (int) (300 / dist);
						color = wall.getColor();
					}
				}
			}
			g2D.setColor(color);
			g2D.drawLine(i, (getHeight() / 2) - height, i, (getHeight() / 2) + height);
		}
		
		g2D.setColor(Color.BLACK);
		g2D.drawString("FPS " + fps, 5, 10);
		
		frameCount++;
	}

	private boolean running;
	private boolean paused;
	private SimpleMaze maze;
	private ArrayList<Wall> mazeWalls;
	private PlayerComponent player;
	private KeyboardInput keyboard;
	private BufferedImage wallImage;
	private BufferedImage floorImage;
	private BufferedImage playerImage;
	private static final long serialVersionUID = 1L;

	private int fps;
	private int frameCount;
	private double interpolation;
}
