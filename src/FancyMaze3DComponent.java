import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JComponent;

/**
 * @author Gabriel
 * 
 */
public class FancyMaze3DComponent extends JComponent {
	/**
	 * Constructs a fancy maze!
	 */
	public FancyMaze3DComponent() {
		goalColor = new Color(0xc1bb00);
		pCS = new PropertyChangeSupport(this);
		new AudioThread().start();

		// Add Game Keyboard
		keyboard = new KeyboardInput();
		addKeyListener(keyboard);

		// Add Generic Keyboard Listeners for pause and mute and exit
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_P && running) {
					if (paused) {
						resume();
					} else {
						pause();
					}
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_M) {
					muted = !muted;
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});

		// Setup fields
		running = false;
		paused = false;
		muted = false;

		// Enable the component
		setDoubleBuffered(true);
		setFocusable(true);

		fps = 60;
	}

	/**
	 * Initialise and start game
	 */
	public void newGame() {
		// Initialise game
		running = true;
		pCS.firePropertyChange("running", !running, running);
		maze = new SimpleMaze(10, 10);
		mazeWalls = maze.toWalls();
		player = new Player();

		// Position end goal
		endWalls = new ArrayList<Wall>();
		endWalls.add(new Wall(new Line(2 * maze.getWidth() - 1.7, 2 * maze
				.getHeight() - 1.7, 2 * maze.getWidth() - 1.2, 2 * maze
				.getHeight() - 1.2), goalColor));
		endWalls.add(new Wall(new Line(2 * maze.getWidth() - 1.2, 2 * maze
				.getHeight() - 1.7, 2 * maze.getWidth() - 1.7, 2 * maze
				.getHeight() - 1.2), goalColor));


		// Get rid of any old instances
		System.gc();

		// Run game
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};

		loop.start();
	}

	/**
	 * Runs game loop
	 */
	private void gameLoop() {
		final double GAME_HERTZ = 30.0;
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final int MAX_UPDATES_BEFORE_RENDER = 3;
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		final double TARGET_FPS = 60.0;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		// Loop until game finishes
		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;

			// Only update if unpaused
			if (!paused) {
				// Do as many game updates as we need to, potentially playing
				// catchup
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
					updateGame();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				// Avoid extreme numbers of updates
				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				// Render
				repaint();
				lastRenderTime = now;

				// Update frames
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime) {
					System.out.println("NEW SECOND " + thisSecond + " "
							+ frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				// Yield until target time
				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
						&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();

					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}

					now = System.nanoTime();
				}
			} else {
				pause();
			}
		}
	}

	/**
	 * Update game state
	 */
	private void updateGame() {
		requestFocusInWindow();
		keyboard.poll();
		player.setVelocity(0);
		processInput();
		player.update(mazeWalls);
		Point p = player.getPosition();
		// Check for win condition
		if (p.getX() > 2 * maze.getWidth() - 2
				&& p.getX() < 2 * maze.getWidth() - 1
				&& p.getY() > 2 * maze.getHeight() - 2
				&& p.getY() < 2 * maze.getHeight() - 1) {
			// WIN!
			pCS.firePropertyChange("winning", false, true);
			running = false;
			pCS.firePropertyChange("running", !running, running);
		}
	}

	/**
	 * Processes input from game keyboard
	 */
	private void processInput() {
		if (keyboard.keyDown(KeyEvent.VK_UP) || keyboard.keyDown(KeyEvent.VK_W)) {
			player.addVelocity(0.05);
		}

		if (keyboard.keyDown(KeyEvent.VK_RIGHT)
				|| keyboard.keyDown(KeyEvent.VK_D)) {
			player.addAngle(Math.PI / 36);
		}

		if (keyboard.keyDown(KeyEvent.VK_DOWN)
				|| keyboard.keyDown(KeyEvent.VK_S)) {
			player.addVelocity(-0.05);
		}

		if (keyboard.keyDown(KeyEvent.VK_LEFT)
				|| keyboard.keyDown(KeyEvent.VK_A)) {
			player.addAngle(-Math.PI / 36);
		}
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Setup graphics
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Avoid special case where paintComponent gets called while not running
		if (!running) {
			return;
		}

		// Draw maze and minimap
		drawMaze3D(g2D);
		drawMinimap(g2D, getWidth() - 220, 16, getWidth() - 16, 220);

		// Draw fps and guide
		g2D.setColor(Color.BLACK);
		g2D.drawString("FPS " + fps, 5, 10);
		g2D.drawString("Press 'p' to pause", 5, 20);

		// Update frame count
		frameCount++;
	}

	/**
	 * Draws the maze
	 * 
	 * @param g
	 *            The graphics to draw on
	 */
	private void drawMaze3D(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		// Background
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(0, 0, getWidth(), getHeight() / 2);
		g2D.setColor(Color.DARK_GRAY);
		g2D.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2 + 1);

		// 3D Craziness (Ray-tracing)
		for (int i = 0; i < getWidth(); i++) {
			// Determine angle of ray
			double j = (i - getWidth() / 2) * (1 / (double) getWidth());
			double rayAngle = player.getAngle() + (Math.atan(j));

			// Determine scaling of distance for the end
			double distScale = Math.sqrt(1 * 1 + j * j);

			// Generate ray
			Line ray = new Line(player.getPosition().getX(), player
					.getPosition().getY(), player.getPosition().getX() + 1000
					* Math.cos(rayAngle), player.getPosition().getY() + 1000
					* Math.sin(rayAngle));

			// Calculate closest wall in the ray
			double minLength = Double.POSITIVE_INFINITY;
			Color color = Color.BLACK;

			// Check walls
			for (Wall wall : mazeWalls) {
				Point p = ray.intersection(wall.getBase());
				if (p != null) {
					double dist = p.distance(player.getPosition());
					if (dist < minLength) {
						minLength = Math.max(dist, 0.1);
						color = wall.getColor();
					}
				}
			}

			// Check endwalls
			Point p = ray.intersection(endWalls.get(0).getBase());
			if (p != null) {
				double dist = p.distance(player.getPosition());
				if (dist < minLength) {
					minLength = Math.max(dist, 0.1);
					color = endWalls.get(0).getColor();
				}
			}
			p = ray.intersection(endWalls.get(1).getBase());
			if (p != null) {
				double dist = p.distance(player.getPosition());
				if (dist < minLength) {
					minLength = Math.max(dist, 0.1);
					color = endWalls.get(1).getColor();
				}
			}


			// Divide length by scale
			minLength /= distScale;

			// Calculate height to draw
			int height = (int) (300 / minLength);

			// Calculate depth shaders
			int newRed = Math.min((int) (color.getRed() / minLength),
					color.getRed());
			int newGreen = Math.min((int) (color.getGreen() / minLength),
					color.getGreen());
			int newBlue = Math.min((int) (color.getBlue() / minLength),
					color.getBlue());

			newRed = Math.max(newRed, color.getRed() / 4);
			newGreen = Math.max(newGreen, color.getGreen() / 4);
			newBlue = Math.max(newBlue, color.getBlue() / 4);

			// Draw line
			color = new Color(newRed, newGreen, newBlue);
			g2D.setColor(color);
			g2D.drawLine(i, (getHeight() / 2) - height, i, (getHeight() / 2)
					+ height);
		}
	}

	/**
	 * Draws the minimap
	 * 
	 * @param g
	 *            The graphics to draw on
	 * @param x1
	 *            The top left x coordinate
	 * @param y1
	 *            The top left y coordinate
	 * @param x2
	 *            The bottom left x coordinate
	 * @param y2
	 *            The bottom right y coordinate
	 * 
	 * @precondition x2 > x1, y2 > y1
	 */
	private void drawMinimap(Graphics g, int x1, int y1, int x2, int y2) {
		Graphics2D g2D = (Graphics2D) g;
		// Move to position
		g2D.translate(x1, y1);
		// Draw background
		g2D.setColor(new Color(255, 255, 255, 63));
		g2D.fillRect(0, 0, x2 - x1 + 1, y2 - y1 + 1);

		// Scale walls to correct draw length
		double[][] scaleArray = {
				{ (x2 - x1) / (maze.getWidth() * 2 - 1.0), 0 },
				{ 0, (y2 - y1) / (maze.getHeight() * 2 - 1.0) } };
		Matrix scaleTransform = new Matrix(scaleArray, scaleArray.length,
				scaleArray[0].length);
		ArrayList<Wall> scaledWalls = new ArrayList<Wall>();
		for (Wall wall : mazeWalls) {
			scaledWalls.add(new Wall(new Line(scaleTransform.multiply(wall
					.getBase())), wall.getColor()));
		}

		// Add walls to path
		Path2D.Float path = new Path2D.Float();
		path.moveTo(0, 0);
		for (Wall wall : scaledWalls) {
			path.lineTo(wall.getBase().getX1(), wall.getBase().getY1());
		}
		path.lineTo(0, 0);

		// Draw path
		g2D.setColor(Color.BLACK);
		g2D.draw(path);
		g2D.setColor(new Color(255, 255, 255, 31));
		g2D.fill(path);

		// Scale and draw player
		player.setScaleTransform(scaleTransform);
		player.draw(g2D);

		// Line scaledEndWall;
		// for (Wall wall : endWalls) {
		// scaledEndWall = new Line(scaleTransform.multiply(wall.getBase()));
		// g2D.setColor(wall.getColor());
		// g2D.drawLine((int) scaledEndWall.getX1(), (int)
		// scaledEndWall.getY1(), (int) scaledEndWall.getX2(), (int)
		// scaledEndWall.getY2());
		// }
		Point goalPosition = new Point(2 * maze.getWidth() - 1.5, 2 * maze
				.getHeight() - 1.5);
		double goalWidth = 0.75;
		double goalHeight = 0.75;
		Quadrilateral goalQuad = new Quadrilateral(scaleTransform.multiply(new Quadrilateral(goalPosition.getX() - goalWidth / 2, goalPosition.getY()
				- goalHeight / 2, goalPosition.getX() + goalWidth / 2, goalPosition.getY()
				- goalHeight / 2, goalPosition.getX() + goalWidth / 2, goalPosition.getY()
				+ goalHeight / 2, goalPosition.getX() - goalWidth / 2, goalPosition.getY()
				+ goalHeight / 2)));
		
		g2D.setColor(goalColor);
		g2D.fillOval((int) goalQuad.getX(1), (int) goalQuad.getY(1),
				(int) (goalQuad.getX(3) - goalQuad.getX(1)),
				(int) (goalQuad.getY(3) - goalQuad.getY(1)));

		// Return graphics to original position
		g2D.translate(-x1, -y1);
	}

	/**
	 * Pauses the game and sends off event
	 */
	public void pause() {
		paused = true;
		pCS.firePropertyChange("paused", !paused, paused);
	}

	/**
	 * Resumes the game
	 */
	public void resume() {
		paused = false;
	}

	/**
	 * Mutes the audio and sends off event
	 */
	public void mute() {
		muted = true;
		pCS.firePropertyChange("muted", !muted, muted);
	}


	/**
	 * Unmutes the audio and sends off event
	 */
	public void unMute() {
		muted = false;
		pCS.firePropertyChange("muted", !muted, muted);
	}


	/**
	 * Getter for muted
	 * 
	 * @return muted
	 */
	public boolean isMuted() {
		return muted;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pCS.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pCS.removePropertyChangeListener(listener);
	}

	/**
	 * Private audio thread class
	 * 
	 * @author Gabriel
	 */
	private class AudioThread extends Thread {
		byte tempBuffer[] = new byte[5];

		/**
		 * Runs the thread
		 */
		public void run() {
			try {
				// PLAY MUSIC!!!
				while (true) {
					// Take input
					audioInputStream = AudioSystem
							.getAudioInputStream(new File("mountain_king.wav"));
					audioFormat = audioInputStream.getFormat();

					// Convert to dataline
					DataLine.Info dataLineInfo = new DataLine.Info(
							SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem
							.getLine(dataLineInfo);

					sourceDataLine.open(audioFormat);

					// Play dataline
					sourceDataLine.start();
					int cnt = audioInputStream.read(tempBuffer, 0,
							tempBuffer.length);
					while (cnt != -1) {
						if (!muted) {
							sourceDataLine.write(tempBuffer, 0, cnt);
						}
						cnt = audioInputStream.read(tempBuffer, 0,
								tempBuffer.length);
					}

					// Close dataline
					sourceDataLine.drain();
					sourceDataLine.close();
				}
			} catch (Exception e) {
				// This should never happen, unless we don't include audio file
				e.printStackTrace();
				System.exit(0);
			}
		}

		// Data
		AudioFormat audioFormat;
		AudioInputStream audioInputStream;
		SourceDataLine sourceDataLine;
	}

	// Flags
	private boolean running;
	private boolean paused;
	private boolean muted;

	// Maze data
	private SimpleMaze maze;
	private ArrayList<Wall> mazeWalls;
	private ArrayList<Wall> endWalls;
	private Player player;

	// Custom keyboard input
	private KeyboardInput keyboard;

	private Color goalColor;

	// Property change event thing
	private PropertyChangeSupport pCS;

	// Frame rate
	private int fps;
	private int frameCount;
	private static final long serialVersionUID = 1L;
}
