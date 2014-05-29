import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	public FancyMaze3DComponent() {
		pCS = new PropertyChangeSupport(this);
		new AudioThread().start();

		keyboard = new KeyboardInput();
		addKeyListener(keyboard);
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

		running = false;
		paused = false;
		muted = false;

		setDoubleBuffered(true);
		setFocusable(true);

		fps = 60;
	}

	public void newGame() {
		running = true;
		pCS.firePropertyChange("running", !running, running);
		maze = new SimpleMaze(10, 10);
		mazeWalls = maze.toWalls();
		player = new Player();
		System.gc();
		
		endWalls = new ArrayList<Wall>();
		endWalls.add(new Wall(new Line(2 * maze.getWidth() - 1.6, 2 * maze
				.getHeight() - 1.6, 2 * maze.getWidth() - 1.4, 2 * maze
				.getHeight() - 1.4), Color.WHITE));
		endWalls.add(new Wall(new Line(2 * maze.getWidth() - 1.4, 2 * maze
				.getHeight() - 1.6, 2 * maze.getWidth() - 1.6, 2 * maze
				.getHeight() - 1.4), Color.WHITE));
		
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

	private void updateGame() {
		requestFocusInWindow();
		keyboard.poll();
		player.setVelocity(0);
		processInput();
		player.update(mazeWalls);
		Point p = player.getPosition();
		if (p.getX() > 2 * maze.getWidth() - 2
				&& p.getX() < 2 * maze.getWidth() - 1
				&& p.getY() > 2 * maze.getHeight() - 2
				&& p.getY() < 2 * maze.getHeight() - 1) {
			running = false;
			pCS.firePropertyChange("running", !running, running);
		}
	}

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
	public void paint(Graphics g) {
		super.paint(g);
		paintComponent(g);
		if (paused || !running) {
			paintChildren(g);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (!running) {
			return;
		}

		drawMaze3D(g2D);
		drawMinimap(g2D, getWidth() - getHeight() / 3 - 10, 10,
				getWidth() - 10, getHeight() / 3 + 10);

		g2D.setColor(Color.BLACK);
		g2D.drawString("FPS " + fps, 5, 10);

		frameCount++;
	}

	private void drawMaze3D(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;

		// Background
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(0, 0, getWidth(), getHeight() / 2);
		g2D.setColor(Color.DARK_GRAY);
		g2D.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2 + 1);

		// 3D Craziness (Ray-tracing)
		for (int i = 0; i < getWidth(); i++) {
			double rayAngle = player.getAngle()
					+ ((i - (getWidth() / 2)) * (Math.PI / 2000));
			Line ray = new Line(player.getPosition().getX(), player
					.getPosition().getY(), player.getPosition().getX() + 1000
					* Math.cos(rayAngle), player.getPosition().getY() + 1000
					* Math.sin(rayAngle));
			double minLength = Double.POSITIVE_INFINITY;
			int height = 300;
			Color color = Color.BLACK;
			for (Wall wall : mazeWalls) {
				Point p = ray.intersection(wall.getBase());
				if (p != null) {
					double dist = p.distance(player.getPosition());
					if (dist < minLength) {
						minLength = Math.max(dist, 0.1);
						height = (int) (300 / dist);
						color = wall.getColor();
					}
				}
			}
			Point p = ray.intersection(endWalls.get(0).getBase());
			if (p != null) {
				double dist = p.distance(player.getPosition());
				if (dist < minLength) {
					minLength = Math.max(dist, 0.1);
					height = (int) (300 / dist);
					color = endWalls.get(0).getColor();
				}
			}
			p = ray.intersection(endWalls.get(1).getBase());
			if (p != null) {
				double dist = p.distance(player.getPosition());
				if (dist < minLength) {
					minLength = Math.max(dist, 0.1);
					height = (int) (300 / dist);
					color = endWalls.get(1).getColor();
				}
			}
			int newRed = Math.min((int) (color.getRed() / minLength), 255);
			int newGreen = Math.min((int) (color.getGreen() / minLength), 255);
			int newBlue = Math.min((int) (color.getBlue() / minLength), 255);
			color = new Color(newRed, newGreen, newBlue);
			g2D.setColor(color);
			g2D.drawLine(i, (getHeight() / 2) - height, i, (getHeight() / 2)
					+ height);
		}
	}

	private void drawMinimap(Graphics g, int x1, int y1, int x2, int y2) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.translate(x1, y1);
		g2D.setColor(new Color(0, 0, 0, 63));
		g2D.fillRect(0, 0, x2 - x1 + 1, y2 - y1 + 1);
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
		Path2D.Float path = new Path2D.Float();
		path.moveTo(0, 0);
		for (Wall wall : scaledWalls) {
			path.lineTo(wall.getBase().getX1(), wall.getBase().getY1());
		}
		path.lineTo(0, 0);
		g2D.setColor(new Color(0, 0, 0, 127));
		g2D.draw(path);
		g2D.setColor(new Color(0, 0, 0, 31));
		g2D.fill(path);
		player.setScaleTransform(scaleTransform);
		player.draw(g2D);
		Line scaledEndWall;
		for (Wall wall : endWalls) {
			scaledEndWall = new Line(scaleTransform.multiply(wall.getBase()));
			g2D.setColor(wall.getColor());
			g2D.drawLine((int) scaledEndWall.getX1(), (int) scaledEndWall.getY1(), (int) scaledEndWall.getX2(), (int) scaledEndWall.getY2());
		}
		g2D.translate(-x1, -y1);
	}

	public void pause() {
		paused = true;
		pCS.firePropertyChange("paused", !paused, paused);
	}

	public void resume() {
		paused = false;
//		quitButton.setEnabled(false);
//		quitButton.setVisible(false);
	}
	
	public void mute() {
		muted = true;
		pCS.firePropertyChange("muted", !muted, muted);
	}
	
	public void unMute() {
		muted = false;
		pCS.firePropertyChange("muted", !muted, muted);
	}
	
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
	
	private class AudioThread extends Thread {
		byte tempBuffer[] = new byte[5];
		public void run() {
			try {
				while (true) {
					audioInputStream = AudioSystem.getAudioInputStream(new File("mountain_king.wav"));
					audioFormat = audioInputStream.getFormat();
					
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					int cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					while (cnt != -1) {
						if (!muted) {
							sourceDataLine.write(tempBuffer, 0, cnt);
						}
						cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
					}
					
					sourceDataLine.drain();
					sourceDataLine.close();
					System.out.println("Audio Closed");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		AudioFormat audioFormat;
		AudioInputStream audioInputStream;
		SourceDataLine sourceDataLine;
	}

	private boolean running;
	private boolean paused;
	private boolean muted;
	private SimpleMaze maze;
	private ArrayList<Wall> mazeWalls;
	private ArrayList<Wall> endWalls;
	private Player player;
	private KeyboardInput keyboard;
	
	private PropertyChangeSupport pCS;

	private int fps;
	private int frameCount;
	private static final long serialVersionUID = 1L;
}
